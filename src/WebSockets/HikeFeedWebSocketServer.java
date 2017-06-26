package WebSockets;

/**
 * Created by Levani on 14.06.2017.
 */
import Database.DataManager;
import Database.HikeFeedSocketDM;
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

@ServerEndpoint(value = "/HikeFeedSocket/{hikeId}", configurator = GetHttpSessionConfigurator.class)
public class HikeFeedWebSocketServer{
    private static Map<Integer, Map<Session, HttpSession>> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();

    /* Private instance variables. */
    private DateFormat dateFormat;
    private Calendar calendar;
    private Gson frontGson;
    private Gson gson;

    @OnOpen
    public void open(Session session, @PathParam("hikeId") int hikeId, EndpointConfig config) {
        if(!connectedSessions.containsKey(hikeId)){
            connectedSessions.put(hikeId, new HashMap<>());
        }
        HttpSession httpsession =  (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        connectedSessions.get(hikeId).put(session, httpsession);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar = Calendar.getInstance();
        frontGson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm:ss").create();
        gson = new Gson();
    }

    @OnClose
    public void close(Session session, @PathParam("hikeId") int hikeId) {connectedSessions.get(hikeId).remove(session);}

    @OnError
    public void onError(Throwable error) {}

    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("hikeId") int hikeId) {
        //JsonObject jsonMessage = reader.readObject();
        Map<String, Object> jsonMessage = frontGson.fromJson(message, Map.class);
        if ("getComment".equals(jsonMessage.get("action"))) {
            addComment(jsonMessage, session, hikeId, "getComment");
        }

        if ("getCommentLike".equals(jsonMessage.get("action"))) {
            addCommentLike(jsonMessage, session, hikeId, "getCommentLike");
        }

        if ("getPost".equals(jsonMessage.get("action"))) {
            addPost(jsonMessage, session, hikeId, "getPost");
        }

        if("getPostLike".equals(jsonMessage.get("action"))){
            addPostLike(jsonMessage, session, hikeId);
        }
    }

    private void addComment(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId, String action){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        String comment = (String)data.get("comment");
        HttpSession httpSession = connectedSessions.get(hikeId).get(session);
        Integer userID = (Integer) httpSession.getAttribute("userID");
        Integer postID = Integer.parseInt((String)data.get("postID"));
        int hikeID = hikeId;
        int privacyType = 2;
        Date currDate = calendar.getTime();
        String time = dateFormat.format(currDate);
        int returnedID = HikeFeedSocketDM.getInstance().addComment(userID, postID, hikeID, comment, privacyType, time);
        MiniUser user = DataManager.getInstance().getUserById(userID);
        Comment com = new Comment(returnedID, comment, postID, user, currDate, 0);
        webSocketHelper.sendToAllConnectedSessions(com, action, hikeId, connectedSessions.get(hikeId).keySet());
    }

    private void addCommentLike(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId, String action){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        HttpSession httpSession = connectedSessions.get(hikeId).get(session);
        Integer userID = (Integer) httpSession.getAttribute("userID");
        Integer commentID = Integer.parseInt((String)data.get("commentID"));
        int postID = Integer.parseInt((String)data.get("postID"));
        int returnedID = HikeFeedSocketDM.getInstance().likeComment(userID, commentID);
        Like like;
        like = new Like(postID, commentID, userID, returnedID != -1);
        webSocketHelper.sendToAllConnectedSessions(like, action, hikeId, connectedSessions.get(hikeId).keySet());
    }

    private void addPost(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId, String action){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        String post = (String)data.get("post");
        HttpSession httpSession = connectedSessions.get(hikeId).get(session);
        Integer userID = (Integer) httpSession.getAttribute("userID");
        int hikeID = hikeId;
        Date currDate = calendar.getTime();
        String time = dateFormat.format(currDate);
        int returnedID = HikeFeedSocketDM.getInstance().writePost(userID, hikeID, post, time);
        MiniUser user = DataManager.getInstance().getUserById(userID);

        Post newPost = new Post(returnedID, post, user, currDate, new ArrayList<>(), 0);
        webSocketHelper.sendToAllConnectedSessions(newPost, action, hikeId, connectedSessions.get(hikeId).keySet());
    }

    private void addPostLike(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        HttpSession httpSession = connectedSessions.get(hikeId).get(session);
        Integer userID = (Integer) httpSession.getAttribute("userID");
        Integer postID = Integer.parseInt((String)data.get("postID"));
        int returnedID = HikeFeedSocketDM.getInstance().likePost(userID, postID);
        if(returnedID == -1){
            data.put("likeResult", "unlike");
        } else{
            data.put("likeResult", "like");
        }
        webSocketHelper.sendToAllConnectedSessions(new Object(), "", hikeId, connectedSessions.get(hikeId).keySet());
    }
}