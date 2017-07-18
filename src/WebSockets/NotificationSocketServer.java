package WebSockets;

/**
 * Created by Levani on 14.06.2017.
 */

import Database.*;
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

@ServerEndpoint(value = "/NotificationSocket/{userId}", configurator = GetHttpSessionConfigurator.class)
public class NotificationSocketServer {
    private static Map<Integer, Session> connectedSessions = new HashMap<>();
    private static WebSocketHelper webSocketHelper = new WebSocketHelper();

    /* Private instance variables. */
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ;
    private Calendar calendar = Calendar.getInstance();
    private Gson frontGson = new GsonBuilder().serializeNulls().create();
    ;
    private Gson gson = new Gson();

    /**
     * Method which gets called when Socket gets opened. Initializes all private variables.
     *
     * @param session current session
     * @param userId  id of user who calls action
     * @param config  private configurations of socket
     */
    @OnOpen
    public void open(Session session, @PathParam("userId") int userId, EndpointConfig config) {
        if (!connectedSessions.containsKey(userId)) {
            connectedSessions.put(userId, session);
        }
        HttpSession httpsession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
    }

    /**
     * Method which gets called when Socket gets closed. Removes current session from all sessions.
     *
     * @param session current session
     * @param userId  id of user who calls action
     */
    @OnClose
    public void close(Session session, @PathParam("userId") int userId) {
        connectedSessions.remove(userId);
    }

    /**
     * Method which gets called when Socket comes across an error.
     */
    @OnError
    public void onError(Throwable error) {
    }


    /**
     * Method which gets called when Socket receives a notification.
     *
     * @param userId id of user who calls action
     */
    public void handleNotification(Map<String, Object> message, int userId) {
        String action = (String) message.get("action");
        calendar = Calendar.getInstance();
        if (action.equals("getComment")) {
            getComment((Map) message.get("data"), userId);
        } else if (action.equals("getCommentLike")) {
            getCommentLike((Map) message.get("data"), userId);
        } else if (action.equals("getRequest")) {
            getRequest((Map) message.get("data"), userId);
        }else if(action.equals("getPostLike")){
            getPostLike((Map) message.get("data"), userId);
        }
    }

    /**
     * sends notification of likes of post to poster
     * @param data date means post like message
     * @param userId user who liked that post
     */
    private void getPostLike(Map<String, Object> data, int userId) {
        int posterID = Integer.parseInt((String)data.get("posterID"));
        if(posterID == userId)return;
        int postId = Integer.parseInt((String) data.get("postID"));
        int type = 4;
        int hikeId = Integer.parseInt((String)data.get("hikeID"));
        String hikeName = HikeDM.getInstance().getHikeById(hikeId).getName();
        Date currDate = calendar.getTime();
        String notificationDate = dateFormat.format(currDate);
        int seen = 0;
        int id = NotificationsDM.getInstance().addNotification(posterID, notificationDate, type, postId, userId, -1, hikeId, hikeName, seen);
        User sender = UserInfoDM.getInstance().getUserByID(userId);
        Notification notification = new Notification(id, posterID, currDate, type, postId, sender, null, hikeId, hikeName, seen);
        sendNotification(notification, posterID, userId);

    }

    /**
     * Sends notification to all followers of post.
     *
     * @param comment id of comment
     * @param userId  id of author of comment
     */
    private void getComment(Map<String, Object> comment, int userId) {
        int postId = Integer.parseInt((String) comment.get("postID"));
        int hikeId = Integer.parseInt((String) comment.get("hikeID"));
        Set<Integer> followers = null;
        if (postId == -1) {
            followers = NotificationsDM.getInstance().getHikeFollowers(hikeId);
        } else {
            followers = NotificationsDM.getInstance().getPostFollowers(postId);
        }
        int type = 2;
        int fromUserId = userId;
        Date currDate = calendar.getTime();
        String notificationDate = dateFormat.format(currDate);
        String hikeName = HikeDM.getInstance().getHikeById(hikeId).getName();
        int seen = 0;
        for (Integer i : followers) {
            if (i == fromUserId) continue;
            int id = NotificationsDM.getInstance().addNotification(i, notificationDate, 2, postId, fromUserId, -1, hikeId, hikeName, seen);
            User sender = UserInfoDM.getInstance().getUserByID(fromUserId);
            Notification notification = new Notification(id, i, currDate, type, postId == -1 ? null : postId, sender, null, hikeId, hikeName, seen);
            sendNotification(notification, i, fromUserId);
        }
    }

    /**
     * Sends notification to all followers of comment.
     *
     * @param commentLike id of like
     * @param userId      id of user who liked comment
     */
    private void getCommentLike(Map<String, Object> commentLike, int userId) {
        int postId = Integer.parseInt((String) commentLike.get("postID"));
        int hikeId = Integer.parseInt((String) commentLike.get("hikeID"));
        int commentID = Integer.parseInt((String) commentLike.get("commentID"));
        int follower = NotificationsDM.getInstance().getCommentFollower(commentID);
        int type = 3;
        int fromUserId = userId;
        Date currDate = calendar.getTime();
        String notificationDate = dateFormat.format(currDate);
        String hikeName = HikeDM.getInstance().getHikeById(hikeId).getName();
        int seen = 0;
        if (follower != fromUserId) {
            int id = NotificationsDM.getInstance().addNotification(follower, notificationDate, 3, postId, fromUserId, -1, hikeId, hikeName, seen);
            User sender = UserInfoDM.getInstance().getUserByID(fromUserId);
            Notification notification = new Notification(id, follower, currDate, type, postId == -1 ? null : postId, sender, null, hikeId, hikeName, seen);
            sendNotification(notification, follower, fromUserId);
        }
    }

    /**
     * Sends notification receiver of request.
     *
     * @param request request
     * @param userId  id of user who liked comment
     */
    private void getRequest(Map<String, Object> request, int userId) {
        int hikeId = Integer.parseInt((String) request.get("hikeID"));
        int follower = Integer.parseInt((String) request.get("followerID"));
        int requestID = Integer.parseInt((String) request.get("requestID"));
        int type = 1;
        Date currDate = calendar.getTime();
        String notificationDate = dateFormat.format(currDate);
        String hikeName = HikeDM.getInstance().getHikeById(hikeId).getName();
        int seen = 0;
        if (follower != userId) {
            int id = NotificationsDM.getInstance().addNotification(follower, notificationDate, 1, -1, userId, requestID, hikeId, hikeName, seen);
            User sender = UserInfoDM.getInstance().getUserByID(userId);
            Notification notification = new Notification(id, follower, currDate, type, null, sender, requestID, hikeId, hikeName, seen);
            sendNotification(notification, follower, userId);
        }
    }

    /**
     * Method which sends notification to user.
     *
     * @param notification
     * @param toUserId
     */
    private void sendNotification(Notification notification, int toUserId, int fromUserId) {
        if (fromUserId == toUserId) {
            return;
        }
        try {
            Session s = connectedSessions.get(toUserId);
            if(s == null) return;
            webSocketHelper.sendToSession(s, frontGson.toJson(notification));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}