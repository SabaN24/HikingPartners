package WebSockets;
import Models.HikeResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import java.io.*;
import java.util.*;
/**
 * Created by Sandro on 17-Jun-17.
 */
public class WebSocketHelper {
    public void sendToAllConnectedSessions(Object data, String action, int hikeId, Set<Session> currentSessions) {
        Gson gson = new GsonBuilder().serializeNulls().create();
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

