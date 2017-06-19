package WebSockets;

/**
 * Created by Levani on 14.06.2017.
 */
import Database.HikeFeedSocketDM;
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint("/HikeFeedSocket/{hikeId}")
public class HikeFeedWebSocketServer{
    private static Map<Integer, Set<Session>> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();
    private static HikeFeedSocketDM socketDM;
    private static DateFormat dateFormat;
    private static DateFormat frontDateFormat;
    private static Calendar calendar;

    @OnOpen
    public void open(Session session, @PathParam("hikeId") int hikeId, EndpointConfig config) {
        if(!connectedSessions.containsKey(hikeId)){
            connectedSessions.put(hikeId, new HashSet<>());
        }
        connectedSessions.get(hikeId).add(session);
        socketDM = new HikeFeedSocketDM();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
        Gson gson = new Gson();
        Map<String, Object> jsonMessage = gson.fromJson(message, Map.class);
        if ("getComment".equals(jsonMessage.get("action"))) {
            addComment(jsonMessage, session, hikeId);
        }

        if ("getCommentLike".equals(jsonMessage.get("action"))) {
            addCommentLike(jsonMessage, session, hikeId);
        }

        if ("getPost".equals(jsonMessage.get("action"))) {
            addPost(jsonMessage, session, hikeId);
        }

        if("getPostLike".equals(jsonMessage.get("action"))){
            addPostLike(jsonMessage, session, hikeId);
        }
    }

    private void addComment(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        String comment = (String)data.get("comment");
        Integer userID = Integer.parseInt((String)data.get("userID"));
        Integer postID = Integer.parseInt((String)data.get("postID"));
        int hikeID = hikeId;
        int privacyType = 2;
        Date currDate = calendar.getTime();
        String time = dateFormat.format(currDate);
        String frontTime = frontDateFormat.format(currDate);
        int returnedID = socketDM.addComment(userID, postID, hikeID, comment, privacyType, time);
        data.put("commentID", returnedID);
        data.put("date", frontTime);
        webSocketHelper.sendToAllConnectedSessions(jsonMessage, hikeId, connectedSessions.get(hikeId));
    }

    private void addCommentLike(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        Integer userID = Integer.parseInt((String)data.get("userID"));
        Integer commentID = Integer.parseInt((String)data.get("commentID"));
        int returnedID = socketDM.likeComment(userID, commentID);
        if(returnedID == -1){
            data.put("likeResult", "unlike");
        } else{
            data.put("likeResult", "like");
        }
        webSocketHelper.sendToAllConnectedSessions(jsonMessage, hikeId, connectedSessions.get(hikeId));
    }

    private void addPost(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        String post = (String)data.get("post");
        Integer userID = Integer.parseInt((String)data.get("userID"));
        int hikeID = hikeId;
        String time = dateFormat.format(calendar.getTime());
        int returnedID = socketDM.writePost(userID, hikeID, post, time);
        data.put("postID", returnedID);
        webSocketHelper.sendToAllConnectedSessions(jsonMessage, hikeId, connectedSessions.get(hikeId));
    }

    private void addPostLike(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        Integer userID = Integer.parseInt((String)data.get("userID"));
        Integer postID = Integer.parseInt((String)data.get("postID"));
        int returnedID = socketDM.likePost(userID, postID);
        if(returnedID == -1){
            data.put("likeResult", "unlike");
        } else{
            data.put("likeResult", "like");
        }
        webSocketHelper.sendToAllConnectedSessions(jsonMessage, hikeId, connectedSessions.get(hikeId));
    }
}