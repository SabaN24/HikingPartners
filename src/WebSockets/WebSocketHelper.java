package WebSockets;
import Models.HikeResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import java.io.*;
import java.util.*;
/**
 * Created by Sandro on 17-Jun-17.
 * Class which communicates with all the users connected to given session.
 */
public class WebSocketHelper {

    /**
     * Sends message to all users connected to given session
     * @param data data which needs to be sent
     * @param action action triggered in socket
     * @param hikeId id of hike
     * @param currentSessions all sessions for all connected users.
     */
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

    /**
     * Sends message to one session only
     * @param session session which receives message
     * @param message message which needs to be sent
     * @throws IOException
     */
    public void sendToSession(Session session, String message) throws IOException{
        session.getBasicRemote().sendText(message);
    }
}

