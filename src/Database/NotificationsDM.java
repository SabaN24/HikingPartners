package Database;

import Models.Notification;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * Created by Saba on 7/16/2017.
 */
public class NotificationsDM {

    /* Private variables */
    private DatabaseConnector databaseConnector;

    public static final String ATTR = "HikeDM";

    private static NotificationsDM notificationsDM = null;

    /**
     * Private constructor which calls getInstance method.
     */
    private NotificationsDM() {
        databaseConnector = DatabaseConnector.getInstance();
    }

    /**
     * getInstance method to make this class Signletone.
     *
     * @return NotificationsDM object
     */
    public static NotificationsDM getInstance() {
        if (notificationsDM == null) {
            notificationsDM = new NotificationsDM();
        }
        return notificationsDM;
    }

    /**
     * Returns list of people who follow given post.
     *
     * @param postId id of post
     * @return list of followers
     */
    public Set<Integer> getPostFollowers(int postId) {
        String query = "Select * from comments where post_ID = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        Set<Integer> followers = getFollowers(preparedStatement, postId);
        followers.add(HikeFeedDM.getInstance().getPostCreator(postId));
        return followers;
    }

    /**
     * Returns id of user who follows given comment.
     *
     * @param commentId id of comment
     * @return id of user who follows given comment
     */
    public int getCommentFollower(int commentId) {
        String query = "Select * from comments where id = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, commentId);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            if (resultSet.next()) {
                int id = resultSet.getInt("user_ID");
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Returns list of people who follow public posts of given hike.
     *
     * @param hikeId id of hike
     * @return list of followers
     */
    public Set<Integer> getHikeFollowers(int hikeId) {
        Set<Integer> followers = new HashSet<>();
        String query = "Select * from comments where hike_id = ? and privacy_type = 1;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        followers = getFollowers(preparedStatement, hikeId);
        return followers;
    }

    /**
     * Executes prepared statement query with given id and builds list of followers.
     *
     * @param preparedStatement statement which needs to be executed
     * @param id                id of post/hike
     * @return list of followers
     */
    private Set<Integer> getFollowers(PreparedStatement preparedStatement, int id) {
        Set<Integer> followers = new HashSet<>();
        try {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            while (resultSet.next()) {
                int current = resultSet.getInt("user_ID");
                followers.add(current);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return followers;
    }

    /**
     * Adds new notification to database with given paremeters.
     *
     * @param receiverId id of user who receives notification
     * @param date       date and time of notification
     * @param typeId     type of notification
     * @param postId     id of post
     * @param senderId   id of user who sent notification
     * @param requestId  id of request
     * @param hikeId     id of hike
     * @param hikeName   name of hike
     * @param seen       whether or not notification is seen
     * @return id of newly added notification
     */
    public int addNotification(int receiverId, String date, int typeId, int postId, int senderId, int requestId, int hikeId, String hikeName, int seen) {
        String query = "Insert into notifications (user_ID, time, type_ID, post_ID, from_user_ID, request_ID, hike_ID, hike_name, seen) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, receiverId);
            preparedStatement.setString(2, date);
            preparedStatement.setInt(3, typeId);
            if (postId == -1) {
                preparedStatement.setNull(4, Types.INTEGER);
            } else {
                preparedStatement.setInt(4, postId);
            }
            preparedStatement.setInt(5, senderId);
            if (requestId == -1) {
                preparedStatement.setNull(6, Types.INTEGER);
            } else {
                preparedStatement.setInt(6, requestId);
            }
            preparedStatement.setInt(7, hikeId);
            preparedStatement.setString(8, hikeName);
            preparedStatement.setInt(9, seen);
            databaseConnector.updateDataWithPreparedStatement(preparedStatement);
            ResultSet resultSet = databaseConnector.getData("select ID from notifications order by ID desc limit 1");
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
