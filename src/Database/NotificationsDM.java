package Database;

import Models.Notification;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * @param postId id of post
     * @return list of followers
     */
    public Set<Integer> getPostFollowers(int postId){
        Set<Integer> followers = new HashSet<>();
        String query = "Select * from comments where post_ID = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            while(resultSet.next()){
                int current = resultSet.getInt("user_ID");
                followers.add(current);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return followers;
    }

    /**
     * Returns id of user who follows given comment.
     * @param commentId id of comment
     * @return id of user who follows given comment
     */
    public int getCommontFollower(int commentId){
        String query = "Select * from comments where comment_ID = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, commentId);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            if(resultSet.next()){
                int id = resultSet.getInt("user_ID");
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
