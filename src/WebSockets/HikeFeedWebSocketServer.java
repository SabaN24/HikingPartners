package WebSockets;

/**
 * Created by Levani on 14.06.2017.
 */
import javax.json.*;
import javax.websocket.*;
import javax.websocket.server.*;
import java.io.*;
import java.util.*;

@ServerEndpoint("/HikeFeedSocket/{hikeId}")
public class HikeFeedWebSocketServer {

    private Map<Integer, Map<String, Session>> connectedSessions = new HashMap<>();

    @OnOpen
    public void open(Session session, @PathParam("hikeId") int hikeId) {
        if(!connectedSessions.containsKey(hikeId)){
            connectedSessions.put(hikeId, new HashMap<>());
        }
        connectedSessions.get(hikeId).put(session.getId(), session);
    }

    @OnClose
    public void close(Session session, @PathParam("hikeId") int hikeId) {
        connectedSessions.get(hikeId).remove(session.getId());
    }

    @OnError
    public void onError(Throwable error) {
    }

    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("hikeId") int hikeId) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("getComment".equals(jsonMessage.getString("action"))) {
                //bazashi komentaris chagdeba
                //komentaris gagzavna yvela am hikeze miertebul sesiastan
            }

            if ("getCommentLike".equals(jsonMessage.getString("action"))) {
                //avsaxot bazashi like;
                //am likeze informaciis gagzavna yvela am hikeze miertebul sesiastan
            }

            if ("getPost".equals(jsonMessage.getString("action"))) {
                //komentaris analogiurad
            }

            if("getPostLike".equals(jsonMessage.getString("action"))){
                //komentaris analogiurad
            }
        }
    }

    private void sendToAllConnectedSessions(JsonObject message, int hikeId, Session curSession) {
        Map<String, Session> currentSessions = connectedSessions.get(hikeId);
        for (Session session : currentSessions.values()) {
            try {
                if(!session.getId().equals(curSession.getId()))
                    sendToSession(session, message);
            }catch(IOException e){
                connectedSessions.remove(hikeId).remove(session.getId());
            }
        }
    }

    private void sendToSession(Session session, JsonObject message) throws IOException{
        session.getBasicRemote().sendText(message.toString());
    }
}