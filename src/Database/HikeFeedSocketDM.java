package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by vache on 6/15/2017.
 */
public class HikeFeedSocketDM {
    private DatabaseConnector databaseConnector;

    /* Constants */
    public static final String ATTR = "SocketDM";

    private static HikeFeedSocketDM socketDM = null;

    private HikeFeedSocketDM() {}

    public static HikeFeedSocketDM getInstance(){
        if(socketDM == null){
            socketDM = new HikeFeedSocketDM();
        }
        return socketDM;
    }
    /**
     * Adding post in database
     *
     * @param userID
     * @param hikeID
     * @param post
     * @return ID of currently added post
     */
    public int writePost(int userID, int hikeID, String post, String time) {
        StringBuilder query = new StringBuilder("insert into posts (post_text, hike_id, user_id, post_time) values(");
        query.append("\"" + post + "\",");
        query.append(hikeID + ", ");
        query.append(userID + ", ");
        query.append("'" + time + "')");
        databaseConnector.updateData(query.toString());
        ResultSet resultSet = databaseConnector.getData("select ID from posts order by ID desc limit 1");
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
     * Adding comment in database
     *
     * @param userID
     * @param postID
     * @param hikeID
     * @param comment
     * @param privacyType
     * @return ID of currently added comment
     */
    public int addComment(int userID, int postID, int hikeID, String comment, int privacyType, String currTime) {
        StringBuilder query = new StringBuilder("insert into comments");
        query.append("(comment_text, hike_ID, user_ID, comment_time, privacy_type, post_ID)");
        query.append("values ( '" + comment + "', " + hikeID + ", " + userID + ", '" + currTime + "'," + privacyType + ", ");
        if (privacyType == 1) {
            query.append("null)");
        } else if (privacyType == 2) {
            query.append("" + postID + ")");
        }
        databaseConnector.updateData(query.toString());
        ResultSet resultSet = databaseConnector.getData("select ID from comments order by ID desc limit 1");
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
     * Adding post like in database
     *
     * @param userID
     * @param postID
     * @return ID of currently added post like
     */
    public int likePost(int userID, int postID) {
        StringBuilder query = new StringBuilder("insert into post_likes (post_ID, user_ID) values(");
        query.append("" + postID + ", ");
        query.append("" + userID + ")");
        databaseConnector.updateData(query.toString());

        if (likeExistsComment(userID, postID)) {
            databaseConnector.updateData("Delete from post_likes where user_ID = " +
                    "\"" + userID + "\" AND post_ID = " + "" + "\"" + postID + "\";");
            return -1;
        }

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
     * Adding comment like in database
     *
     * @param userID
     * @param commentID
     * @return ID of currently added comment like
     */
    public int likeComment(int userID, int commentID) {
        StringBuilder query = new StringBuilder("insert into comment_likes (comment_ID, user_ID) values(");
        query.append("" + commentID + ", ");
        query.append("" + userID + ")");
        if (likeExistsComment(userID, commentID)) {
            databaseConnector.updateData("Delete from comment_likes where user_ID = " +
                    "\"" + userID + "\" AND comment_ID = " + "" + "\"" + commentID + "\";");
            return -1;
        }
        databaseConnector.updateData(query.toString());
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
     * Checks if given table contains given value.
     *
     * @param userID id of user
     * @param commentID id of comment
     * @return boolean depending on result of search.
     */
    public boolean likeExistsComment(int userID, int commentID) {
        ResultSet rs = databaseConnector.getData("Select Count(ID) count from comment_likes where user_ID = " +
                "\"" + userID + "\" AND comment_ID = " + "" + "\"" + commentID + "\";");
        int rows = 0;
        try {
            if(rs.next()){
                rows = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows != 0;
    }

    public boolean likeExistsPost(int userID, int postID){
        ResultSet rs = databaseConnector.getData("Select Count(ID) count from post_likes where user_ID = " +
                "\"" + userID + "\" AND post_ID = " + "" + "\"" + postID + "\";");
        int rows = 0;
        try {
            if(rs.next()){
                rows = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows != 0;
    }
}