package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by vache on 6/15/2017.
 * DataManager class which is responsible for posts in database.
 */
public class HikeFeedDM {
    private DatabaseConnector databaseConnector;

    /* Constants */
    public static final String ATTR = "SocketDM";

    private static HikeFeedDM socketDM = null;


    /**
     * Private constructor of HikeFeedDM object (Singletone pattern)
     */
    private HikeFeedDM() {
        databaseConnector = DatabaseConnector.getInstance();
    }


    /**
     * getInstance method so that class is singletone.
     *
     * @return HikeFeedDM object
     */
    public static HikeFeedDM getInstance() {
        if (socketDM == null) {
            socketDM = new HikeFeedDM();
        }
        return socketDM;
    }

    /**
     * Adds post in database with given parameters.
     *
     * @param userID  id of user who created post
     * @param hikeID  id of hike where post was created
     * @param post    text of post
     * @param time    time when post was created
     * @param link    link of youtube video (if exists)
     * @param photoID id of photo which was uploaded
     * @return ID of currently added post
     */
    public int writePost(int userID, int hikeID, String post, String time, String link, int photoID) {
        String query = "insert into posts (post_text, link, hike_id, user_id, post_time, photo_ID) values(?, ?, ?, ?, ?, ?);";
        String photo = photoID == -1 ? "null" : photoID + "";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setString(1, post);
            preparedStatement.setString(2, link);
            preparedStatement.setInt(3, hikeID);
            preparedStatement.setInt(4, userID);
            preparedStatement.setString(5, time);
            if(photoID == -1){
                preparedStatement.setNull(6,  java.sql.Types.INTEGER);
            }else {
                preparedStatement.setInt(6,  photoID);
            }
            databaseConnector.updateDataWithPreparedStatement(preparedStatement);
            ResultSet resultSet = databaseConnector.getData("select ID from posts order by ID desc limit 1");
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adds comment in database with given parameters.
     *
     * @param userID      id of user who created comment
     * @param hikeID      id of hike where comment was created
     * @param comment     text of comment
     * @param privacyType type of privacy of comment (private or public)
     * @param currTime    time when comment was created
     * @return ID of currently added comment
     */
    public int addComment(int userID, int postID, int hikeID, String comment, int privacyType, String currTime) {
        String query = "insert into comments (comment_text, hike_ID, user_ID, comment_time, privacy_type, post_ID) values(?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
            preparedStatement.setString(1, comment);
            preparedStatement.setInt(2, hikeID);
            preparedStatement.setInt(3, userID);
            preparedStatement.setString(4, currTime);
            preparedStatement.setInt(5, privacyType);
            if (privacyType == 1) {
                preparedStatement.setString(6, null);
            } else if (privacyType == 2) {
                preparedStatement.setInt(6, postID);
            }
            databaseConnector.updateDataWithPreparedStatement(preparedStatement);
            ResultSet resultSet = databaseConnector.getData("select ID from comments order by ID desc limit 1");
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adds like of post to database.
     *
     * @param userID id who liked post
     * @param postID post which has been liked
     * @return ID of currently added post like
     */
    public int likePost(int userID, int postID) {
        String query = "insert into post_likes (post_ID, user_ID) values(?, ?);";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, postID);
            preparedStatement.setInt(2, userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (likeExistsPost(userID, postID)) {
            String newQuery = "Delete from post_likes where user_ID = ? AND post_ID = ?;";
            PreparedStatement preparedStatement1 = databaseConnector.getPreparedStatement(newQuery);
            try {
                preparedStatement1.setInt(1, userID);
                preparedStatement1.setInt(2, postID);
                databaseConnector.updateDataWithPreparedStatement(preparedStatement1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }
        databaseConnector.updateDataWithPreparedStatement(preparedStatement);
        ResultSet resultSet = databaseConnector.getData("select ID from post_likes order by ID desc limit 1");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * Adds like of comment to database.
     *
     * @param userID    id who liked comment
     * @param commentID comment which has been liked
     * @return ID of currently added comment like
     */
    public int likeComment(int userID, int commentID) {
        String query = "insert into comment_likes (comment_ID, user_ID) values(?, ?);";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, commentID);
            preparedStatement.setInt(2, userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (likeExistsComment(userID, commentID)) {
            String newQuery = "Delete from comment_likes where user_ID = ? AND comment_ID = ?;";
            PreparedStatement preparedStatement1 = databaseConnector.getPreparedStatement(newQuery);
            try {
                preparedStatement1.setInt(1, userID);
                preparedStatement1.setInt(2, commentID);
                databaseConnector.updateDataWithPreparedStatement(preparedStatement1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }
        databaseConnector.updateDataWithPreparedStatement(preparedStatement);
        ResultSet resultSet = databaseConnector.getData("select ID from comment_likes order by ID desc limit 1");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Checks if comment has been liked by given user
     *
     * @param userID    id of user
     * @param commentID id of comment
     * @return boolean depending on result of search.
     */
    public boolean likeExistsComment(int userID, int commentID) {
        String query = "Select Count(ID) count from comment_likes where user_ID = ? AND comment_ID = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        int rows = 0;
        try {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, commentID);
            ResultSet rs = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            if (rs.next()) {
                rows = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows != 0;
    }

    /**
     * Checks if post has been liked by given user
     *
     * @param userID id of user
     * @param postID id of comment
     * @return boolean depending on result of search.
     */
    public boolean likeExistsPost(int userID, int postID) {
        String query = "Select Count(ID) count from post_likes where user_ID = ? AND post_ID = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        int rows = 0;
        try {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, postID);
            ResultSet rs = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            if (rs.next()) {
                rows = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows != 0;
    }

    /**
     * Retunrs id of creator of given post.
     * @param postId id of desired post
     * @return id of creator
     */
    public int getPostCreator(int postId){
        String query = "Select user_Id from posts where id = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            if(resultSet.next()) {
                int creator = resultSet.getInt("user_ID");
                return creator;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}