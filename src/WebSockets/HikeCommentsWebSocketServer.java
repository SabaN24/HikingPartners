package WebSockets;

/**
 * Created by Sandro on 17.06.2017.
 */
import Database.HikeFeedSocketDM;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import javax.websocket.server.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint("/HikeCommentsSocket/{hikeId}")
public class HikeCommentsWebSocketServer {
    private static Map<Integer, Map<String, Session>> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();
    private static HikeFeedSocketDM socketDM;

    /* Private instance variables. */
    private DateFormat dateFormat;
    private Calendar calendar;

    @OnOpen
    public void open(Session session, @PathParam("hikeId") int hikeId) {
        if(!connectedSessions.containsKey(hikeId)){
            connectedSessions.put(hikeId, new HashMap<>());
        }
        connectedSessions.get(hikeId).put(session.getId(), session);
        socketDM = new HikeFeedSocketDM();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        calendar = Calendar.getInstance();
    }

    @OnClose
    public void close(Session session, @PathParam("hikeId") int hikeId) {connectedSessions.get(hikeId).remove(session.getId());}

    @OnError
    public void onError(Throwable error) {}

    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("hikeId") int hikeId) {
        //JsonObject jsonMessage = reader.readObject();
        Gson gson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm").create();
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
        String time = dateFormat.format(calendar.getTime());
        int returnedID = socketDM.addComment(userID, postID, hikeID, comment, privacyType, time);
        data.put("commentID", returnedID);
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