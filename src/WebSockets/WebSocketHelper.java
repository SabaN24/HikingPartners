package WebSockets;
import Models.Comment;
import Models.HikeResponse;
import Models.MiniUser;
import Models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import javafx.util.Pair;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.*;
import java.io.*;
import java.util.*;
/**
 * Created by Sandro on 17-Jun-17.
 */
public class WebSocketHelper {
    public void sendToAllConnectedSessions(Object data, String action, int hikeId, Set<Session> currentSessions) {
        Gson gson = new GsonBuilder().setDateFormat("MMM, d, yyyy HH:mm:ss").create();
        HikeResponse response = new HikeResponse(data, action);
        String message = gson.toJson(response);
        for (Session session : currentSessions) {
            try {
                sendToSession(session, message);
            }catch(IOException e){
                currentSessions.remove(session);
            }
        }
    }

    public void sendToSession(Session session, String message) throws IOException{
        session.getBasicRemote().sendText(message);
    }
}

