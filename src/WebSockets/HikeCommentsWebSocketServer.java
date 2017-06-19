package WebSockets;

/**
 * Created by Sandro on 17.06.2017.
 */
import Database.HikeFeedSocketDM;
import Listeners.GetHttpSessionConfigurator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint(value = "/HikeCommentsSocket/{hikeId}", configurator = GetHttpSessionConfigurator.class)
public class HikeCommentsWebSocketServer {
    private static Map<Integer, Set<Session>> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();
    private static HikeFeedSocketDM socketDM;

    /* Private instance variables. */
    private DateFormat dateFormat;
    private DateFormat frontDateFormat;
    private Calendar calendar;

    @OnOpen
    public void open(Session session, @PathParam("hikeId") int hikeId, EndpointConfig config) {
        if(!connectedSessions.containsKey(hikeId)){
            connectedSessions.put(hikeId, new HashSet<>());
        }

        connectedSessions.get(hikeId).add(session);
        socketDM = new HikeFeedSocketDM();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        frontDateFormat = new SimpleDateFormat("MMM, d, yyyy HH:mm:ss");
        calendar = Calendar.getInstance();
    }

    @OnClose
    public void close(Session session, @PathParam("hikeId") int hikeId) {connectedSessions.get(hikeId).remove(session.getId());}

    @OnError
    public void onError(Throwable error) {}

    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("hikeId") int hikeId) {
        //JsonObject jsonMessage = reader.readObject();
        Gson gson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm:ss").create();
        Map<String, Object> jsonMessage = gson.fromJson(message, Map.class);
        if ("getComment".equals(jsonMessage.get("action"))) {
            addComment(jsonMessage, session, hikeId);
        }
        if ("getCommentLike".equals(jsonMessage.get("action"))) {
            getCommentLike(jsonMessage, session, hikeId);
        }
    }

    private void addComment(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        String comment = (String)data.get("comment");
        Integer userID = Integer.parseInt((String)data.get("userID"));
        int postID = -1;   //postID doesn't matters because its public Post so it will automatically add "null" in database in postID place.
        int hikeID = hikeId;
        int privacyType = 1;
        Date currDate = calendar.getTime();
        String time = dateFormat.format(currDate);
        String frontTime = frontDateFormat.format(currDate);
        int returnedID = socketDM.addComment(userID, postID, hikeID, comment, privacyType, time);
        data.put("commentID", returnedID);
        data.put("date", frontTime);
        webSocketHelper.sendToAllConnectedSessions(jsonMessage, hikeId, connectedSessions.get(hikeId));
    }

    private void getCommentLike(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId) {
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        Integer userID = Integer.parseInt((String)data.get("userID"));
        Integer commentID = Integer.parseInt((String)data.get("commentID"));
        int returnedID = socketDM.likeComment(userID, commentID);
        if(returnedID == -1){
            data.put("likeResult", "unlike");
        } else {
            data.put("likeResult", "like");
        }
        webSocketHelper.sendToAllConnectedSessions(jsonMessage, hikeId, connectedSessions.get(hikeId));
    }

}