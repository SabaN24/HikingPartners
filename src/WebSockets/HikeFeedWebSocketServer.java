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

@ServerEndpoint(value = "/HikeFeedSocket/{hikeId}", configurator = GetHttpSessionConfigurator.class)
public class HikeFeedWebSocketServer{
    private static Map<Integer, Map<Session, HttpSession>> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();
    private static NotificationSocketServer notificationSocketServer = new NotificationSocketServer();

    /* Private instance variables. */
    private DateFormat dateFormat;
    private Calendar calendar;
    private Gson frontGson;
    private Gson gson;

    /**
     * Method which gets called when Socket gets opened. Initializes all private variables.
     * @param session current session
     * @param hikeId id of hike
     * @param config private configurations of socket
     */
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

    /**
     * Method which gets called when Socket gets closed. Removes current session from all sessions.
     * @param session current session
     * @param hikeId id of hike
     */
    @OnClose
    public void close(Session session, @PathParam("hikeId") int hikeId) {connectedSessions.get(hikeId).remove(session);}

    /**
     * Method which gets called when Socket comes up with en error.
     */
    @OnError
    public void onError(Throwable error) {}

    /**
     * Method which gets called when Socket receives a message.
     * @param session current session
     * @param hikeId id of hike
     */
    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("hikeId") int hikeId) {
        //JsonObject jsonMessage = reader.readObject();
        calendar = Calendar.getInstance();
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

    /**
     * Adds comment to feed of all connected sessions.
     * @param jsonMessage message which needs to be sent
     * @param session sessions
     * @param hikeId id of hike
     * @param action type of action
     */
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
        int returnedID = HikeFeedDM.getInstance().addComment(userID, postID, hikeID, comment, privacyType, time);
        User user = MainDM.getInstance().getUserById(userID);
        Comment com = new Comment(returnedID, comment, postID, user, currDate, 0);
        webSocketHelper.sendToAllConnectedSessions(com, action, hikeId, connectedSessions.get(hikeId).keySet());
    }

    /**
     * Adds comment like to feed of all connected sessions.
     * @param jsonMessage message which needs to be sent
     * @param session sessions
     * @param hikeId id of hike
     * @param action type of action
     */
    private void addCommentLike(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId, String action){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        HttpSession httpSession = connectedSessions.get(hikeId).get(session);
        Integer userID = (Integer) httpSession.getAttribute("userID");
        Integer commentID = Integer.parseInt((String)data.get("commentID"));
        int postID = Integer.parseInt((String)data.get("postID"));
        int returnedID = HikeFeedDM.getInstance().likeComment(userID, commentID);
        Like like;
        like = new Like(postID, commentID, userID, returnedID != -1);
        webSocketHelper.sendToAllConnectedSessions(like, action, hikeId, connectedSessions.get(hikeId).keySet());
    }

    /**
     * Adds post to feed of all connected sessions.
     * @param jsonMessage message which needs to be sent
     * @param session sessions
     * @param hikeId id of hike
     * @param action type of action
     */
    private void addPost(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId, String action){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        String post = (String)data.get("post");
        String link = (String)data.get("link");
        String photoPath = (String)data.get("photoPath");
        HttpSession httpSession = connectedSessions.get(hikeId).get(session);
        Integer userID = (Integer) httpSession.getAttribute("userID");
        int hikeID = hikeId;
        Date currDate = calendar.getTime();
        String time = dateFormat.format(currDate);
        int photoID = -1;
        if(!photoPath.equals("null")){
            photoID = GalleryDM.getInstance().savePhoto(hikeID, userID, photoPath);
        }
        int returnedID = HikeFeedDM.getInstance().writePost(userID, hikeID, post, time, link, photoID);
        User user = MainDM.getInstance().getUserById(userID);
        Photo photo = new Photo(photoID, photoPath, "");
        Post newPost = new Post(returnedID, post, link, user, currDate, new ArrayList<>(), 0, photo);
        webSocketHelper.sendToAllConnectedSessions(newPost, action, hikeId, connectedSessions.get(hikeId).keySet());
    }

    /**
     * Adds post like to feed of all connected sessions.
     * @param jsonMessage message which needs to be sent
     * @param session sessions
     * @param hikeId id of hike
     */
    private void addPostLike(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        HttpSession httpSession = connectedSessions.get(hikeId).get(session);
        Integer userID = (Integer) httpSession.getAttribute("userID");
        Integer postID = Integer.parseInt((String)data.get("postID"));
        int returnedID = HikeFeedDM.getInstance().likePost(userID, postID);
        if(returnedID == -1){
            data.put("likeResult", "unlike");
        } else{
            data.put("likeResult", "like");
        }
        webSocketHelper.sendToAllConnectedSessions(new Object(), "", hikeId, connectedSessions.get(hikeId).keySet());
    }
}