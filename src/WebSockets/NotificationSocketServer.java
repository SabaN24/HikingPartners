package WebSockets;

/**
 * Created by Levani on 14.06.2017.
 */
import Database.GalleryDM;
import Database.MainDM;
import Database.HikeFeedDM;
import Listeners.GetHttpSessionConfigurator;
import Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint(value = "/NotificationSocket/{userId}", configurator = GetHttpSessionConfigurator.class)
public class NotificationSocketServer {
    private static Map<Integer, Session> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();

    /* Private instance variables. */
    private DateFormat dateFormat;
    private Calendar calendar;
    private Gson frontGson;
    private Gson gson;

    /**
     * Method which gets called when Socket gets opened. Initializes all private variables.
     *
     * @param session current session
     * @param userId  id of user who calls action
     * @param config  private configurations of socket
     */
    @OnOpen
    public void open(Session session, @PathParam("userId") int userId, EndpointConfig config) {
        if(!connectedSessions.containsKey(userId)){
            connectedSessions.put(userId, session);
        }
        HttpSession httpsession =  (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar = Calendar.getInstance();
        frontGson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm:ss").create();
        gson = new Gson();
    }

    /**
     * Method which gets called when Socket gets closed. Removes current session from all sessions.
     *
     * @param session current session
     * @param userId  id of user who calls action
     */
    @OnClose
    public void close(Session session, @PathParam("userId") int userId) {
        connectedSessions.remove(userId);
    }

    /**
     * Method which gets called when Socket comes across an error.
     */
    @OnError
    public void onError(Throwable error) {
    }

    /**
     * Method which gets called when Socket receives a message.
     *
     * @param session current session
     * @param userId  id of user who calls action
     */
    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("userId") int userId) {

    }

    public static void handleNotification(Map<String, Object> message, int userId){
        String action = (String)message.get("action");
        if(action.equals("getComment")){
            getComment((Map)message.get("data"), userId);
        }else if(action.equals("getCommentLike")){
            getCommentLike((Map)message.get("data"), userId);
        }else if(action.equals("getRequest")){
            getRequest((Map)message.get("data"), userId);
        }
    }

    private static void getComment(Map<String, Object> comment, int userId) {

    }

    private static void getCommentLike(Map<String, Object> commentLike, int userId) {

    }

    private static void getRequest(Map<String, Object> request, int userId) {

    }

    /**
     * Method which sends notification to user.
     *
     * @param notification
     * @param toUserId
     */
    private void sendNotification(Notification notification, int toUserId){
        try {
            webSocketHelper.sendToSession(connectedSessions.get(toUserId), frontGson.toJson(notification));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}