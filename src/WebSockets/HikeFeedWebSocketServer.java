package WebSockets;

/**
 * Created by Levani on 14.06.2017.
 */
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.*;
import java.io.*;
import java.util.*;

@ServerEndpoint("/HikeFeedSocket/{hikeId}")
public class HikeFeedWebSocketServer {

    public class Response{
        private String action;
        private Map<String, String> data;
        public String getAction(){
            return action;
        }

        public String getDataElement(String key){
            return data.get(key);
        }
    }

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
        Gson gson = new Gson();
        Response response = gson.fromJson(message, Response.class);
        if ("getComment".equals(response.getAction())) {
            //bazashi komentaris chagdeba
            //komentaris gagzavna yvela am hikeze miertebul sesiastan
        }

        if ("getCommentLike".equals(response.getAction())) {
            //avsaxot bazashi like;
            //am likeze informaciis gagzavna yvela am hikeze miertebul sesiastan
        }

        if ("getPost".equals(response.getAction())) {
            //komentaris analogiurad
        }

        if("getPostLike".equals(response.getAction())){
            //komentaris analogiurad
        }
    }

    private void sendToAllConnectedSessions(Response message, int hikeId, Session curSession) {
        Map<String, Session> currentSessions = connectedSessions.get(hikeId);
        Gson gson = new Gson();
        for (Session session : currentSessions.values()) {
            try {
                if(!session.getId().equals(curSession.getId()))
                    sendToSession(session, gson.toJson(message));
            }catch(IOException e){
                connectedSessions.remove(hikeId).remove(session.getId());
            }
        }
    }

    private void sendToSession(Session session, String message) throws IOException{
        session.getBasicRemote().sendText(message);
    }
}