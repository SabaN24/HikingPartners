
package WebSockets;

import Database.ChatDM;
import Database.MainDM;
import Listeners.GetHttpSessionConfigurator;
import Models.Message;

import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Websocket responsible for exchange of messages in chat
 */
@ServerEndpoint(value = "/MessagesSocket/{userId}", configurator = GetHttpSessionConfigurator.class)
public class MessagesWebSocketServer {

    private static HashMap<Integer, Set<Session>> connectedSessions = new HashMap<>();//users - sessions
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();

    /* Private instance variables. */
    private DateFormat dateFormat;
    private Calendar calendar;
    private Gson frontGson;
    private Gson gson;

    /**
     * Method which gets called when Socket gets opened. Initializes all private variables.
     * @param session current session
     * @param userId id of user
     * @param config private configurations of socket
     */
    @OnOpen
    public void open(Session session, @PathParam("userId") int userId, EndpointConfig config) {
        if (!connectedSessions.containsKey(userId)) {
            connectedSessions.put(userId, new HashSet<>());
        }
        connectedSessions.get(userId).add(session);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar = Calendar.getInstance();
        frontGson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm:ss").create();
        gson = new Gson();
    }


    /**
     * Method which gets called when Socket gets closed. Removes current session from all sessions.
     * @param session current session
     * @param userId id of user
     */
    @OnClose
    public void close(Session session, @PathParam("userId") int userId) {
        connectedSessions.get(userId).remove(session);
        if (connectedSessions.get(userId).size() == 0)
            connectedSessions.remove(userId);
    }


    /**
     * Method which gets called when Socket comes up with en error.
     */
    @OnError
    public void onError(Throwable error) {
    }

    /**
     * Method which gets called when Socket receives a message.
     * @param session current session
     * @param userId id of user
     */
    @OnMessage
    public void handleMessage(String message, @PathParam("userId") int userId, Session session) {
        calendar = Calendar.getInstance();
        Map<String, Object> jsonMessage = frontGson.fromJson(message, Map.class);

        if ("getMessage".equals(jsonMessage.get("action"))) {
            addMessage(jsonMessage, userId, session, "getMessage");
        } else if ("openChat".equals(jsonMessage.get("action"))) {
            addChat(jsonMessage, userId, session, "openChat");
        }

    }


    /**
     * Adds new conversation to all connected sessions.
     * @param jsonMessage jsonMessage to retrieve data from front-end
     * @param userId id of user who called action
     * @param session current socket session
     * @param action action which has been called
     */
    private void addChat(Map<String, Object> jsonMessage, int userId, Session session, String action) {
        Integer toUserId = Integer.parseInt((String) jsonMessage.get("toUserId"));
        User userTo = MainDM.getInstance().getUserById(toUserId);
        webSocketHelper.sendToAllConnectedSessions(userTo, action, 1, connectedSessions.get(userId));
    }


    /**
     * Adds a new message to connected chats
     * @param jsonMessage jsonMessage to retrieve data from front-end
     * @param userId id of user who called action
     * @param session current socket session
     * @param action action which has been called
     */
    private void addMessage(Map<String, Object> jsonMessage, int userId, Session session, String action) {
        Map<String, Object> data = (Map) (jsonMessage.get("data"));
        String message = (String) data.get("message");
        Integer fromUserId = userId;
        Integer toUserId = Integer.parseInt((String) data.get("toUserId"));
        Date currDate = calendar.getTime();
        String time = dateFormat.format(currDate);
        int returnId = ChatDM.getInstance().addMessage(fromUserId, toUserId, message, time);
        User userFrom = MainDM.getInstance().getUserById(fromUserId);
        User userTo = MainDM.getInstance().getUserById(toUserId);
        Message mes = new Message(returnId, userFrom, userTo, message, currDate);
        webSocketHelper.sendToAllConnectedSessions(mes, "sendMessage", 1, connectedSessions.get(fromUserId));
        if (connectedSessions.containsKey(toUserId)) {//means user is online and send online
            webSocketHelper.sendToAllConnectedSessions(mes, action, 1, connectedSessions.get(toUserId));
        } else {
            //means user is not online and so open offline user chat when logs in chat will be opened
            ChatDM.getInstance().openChat(toUserId, fromUserId);
        }
    }


}
