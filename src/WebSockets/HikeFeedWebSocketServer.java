package WebSockets;

/**
 * Created by Levani on 14.06.2017.
 */
import Models.Comment;
import Models.MiniUser;
import Models.Post;
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.*;
import java.util.*;

@ServerEndpoint("/HikeFeedSocket/{hikeId}")
public class HikeFeedWebSocketServer{
    private static Map<Integer, Set<Session>> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();

    @OnOpen
    public void open(Session session, @PathParam("hikeId") int hikeId) {
        if(!connectedSessions.containsKey(hikeId)){
            connectedSessions.put(hikeId, new HashSet<>());
        }
        connectedSessions.get(hikeId).add(session);
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
            //bazashi komentaris chagdeba
            //komentaris gagzavna yvela am hikeze miertebul sesiastan
        }

        if ("getCommentLike".equals(jsonMessage.get("action"))) {
            //avsaxot bazashi like;
            //am likeze informaciis gagzavna yvela am hikeze miertebul sesiastan
        }

        if ("getPost".equals(jsonMessage.get("action"))) {
            //komentaris analogiurad
        }

        if("getPostLike".equals(jsonMessage.get("action"))){
            //komentaris analogiurad
        }
    }
}