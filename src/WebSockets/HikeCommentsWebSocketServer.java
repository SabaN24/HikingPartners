package WebSockets;

/**
 * Created by Sandro on 17.06.2017.
 */
import Database.HikeFeedSocketDM;
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.*;
import java.util.*;

@ServerEndpoint("/HikeCommentsSocket/{hikeId}")
public class HikeCommentsWebSocketServer {
    private static Map<Integer, Map<String, Session>> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();
    private static HikeFeedSocketDM socketDM;

    @OnOpen
    public void open(Session session, @PathParam("hikeId") int hikeId) {
        if(!connectedSessions.containsKey(hikeId)){
            connectedSessions.put(hikeId, new HashMap<>());
        }
        connectedSessions.get(hikeId).put(session.getId(), session);
        socketDM = new HikeFeedSocketDM();
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

            Map<String, Object> data = (Map)(jsonMessage.get("data"));

            String comment = (String)data.get("comment");
            Integer userID = Integer.parseInt((String)data.get("userID"));
            int postID = -1;   //postID doesn't matters because its public Post so it will automatically add "null" in database in postID place.
            int hikeID = hikeId;
            int privacyType = 1;
            int returnedID = socketDM.addComment(userID, postID, hikeID, comment, privacyType);

            data.put("commentID", returnedID);
            webSocketHelper.sendToAllConnectedSessions(jsonMessage, hikeId, connectedSessions.get(hikeId));
        }

        if ("getCommentLike".equals(jsonMessage.get("action"))) {
            //avsaxot bazashi like;
            //am likeze informaciis gagzavna yvela am hikeze miertebul sesiastan
        }
    }
}