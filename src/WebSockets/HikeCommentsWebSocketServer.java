package WebSockets;

/**
 * Created by Sandro on 17.06.2017.
 */
import Database.DataManager;
import Database.HikeFeedSocketDM;
import Listeners.GetHttpSessionConfigurator;
import Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint(value = "/HikeCommentsSocket/{hikeId}", configurator = GetHttpSessionConfigurator.class)
public class HikeCommentsWebSocketServer {
    private static Map<Integer, Set<Session>> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();

    /* Private instance variables. */
    private DateFormat dateFormat;
    private Calendar calendar;
    private Gson frontGson;
    private Gson gson;

    @OnOpen
    public void open(Session session, @PathParam("hikeId") int hikeId, EndpointConfig config) {
        if(!connectedSessions.containsKey(hikeId)){
            connectedSessions.put(hikeId, new HashSet<>());
        }
        connectedSessions.get(hikeId).add(session);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar = Calendar.getInstance();
        gson = new Gson();
        frontGson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm:ss").create();
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
    }

    private void addComment(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId, String action){
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        String comment = (String)data.get("comment");
        Integer userID = Integer.parseInt((String)data.get("userID"));
        int postID = -1;   //postID doesn't matters because its public Post so it will automatically add "null" in database in postID place.
        int hikeID = hikeId;
        int privacyType = 1;
        Date currDate = calendar.getTime();
        String time = dateFormat.format(currDate);
        int returnedID = HikeFeedSocketDM.getInstance().addComment(userID, postID, hikeID, comment, privacyType, time);
        MiniUser user = DataManager.getInstance().getUserById(userID);
        Comment comm = new Comment(returnedID, comment, postID, user, currDate, 0);
        webSocketHelper.sendToAllConnectedSessions(comm, action, hikeId, connectedSessions.get(hikeId));
    }

    private void addCommentLike(Map<String, Object> jsonMessage, Session session, @PathParam("hikeId") int hikeId, String action) {
        Map<String, Object> data = (Map)(jsonMessage.get("data"));
        Integer userID = Integer.parseInt((String)data.get("userID"));
        Integer commentID = Integer.parseInt((String)data.get("commentID"));
        int returnedID = HikeFeedSocketDM.getInstance().likeComment(userID, commentID);

        Like like;
        if(returnedID == -1){
            like = new Like(commentID, userID, false);
        }else {
            like = new Like(commentID, userID, true);
        }
        webSocketHelper.sendToAllConnectedSessions(like, action, hikeId, connectedSessions.get(hikeId));
    }

}