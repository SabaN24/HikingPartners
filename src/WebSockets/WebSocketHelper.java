package WebSockets;
import Models.Comment;
import Models.MiniUser;
import Models.Post;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import javax.websocket.*;
import javax.websocket.server.*;
import java.io.*;
import java.util.*;
/**
 * Created by Sandro on 17-Jun-17.
 */
public class WebSocketHelper {
    public void sendToAllConnectedSessions(Map message, int hikeId, Map<String, Session> currentSessions) {
        Gson gson = new Gson();

        for (Session session : currentSessions.values()) {
            try {
                sendToSession(session, gson.toJson(message));
            }catch(IOException e){
                currentSessions.remove(session.getId());
            }
        }
    }

    public void sendToSession(Session session, String message) throws IOException{
        session.getBasicRemote().sendText(message);
    }
}

