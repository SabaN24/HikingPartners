package Database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by vache on 6/15/2017.
 */
public class HikeFeedSocketDM {
    private DatabaseConnector databaseConnector;
    private DateFormat format;
    private DateFormat dateFormat;
    private Calendar calendar;
    public static final String ATTR = "ocketDM";

    public HikeFeedSocketDM() {
        databaseConnector = DatabaseConnector.getInstance();
        format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar = Calendar.getInstance();
    }

    /**
     * Adding post in database
     *
     * @param userID
     * @param hikeID
     * @param post
     * @return ID of currently added post
     */
    public int writePost(int userID, int hikeID, String post) {
        StringBuilder query = new StringBuilder("insert into posts (post_text, hike_id, user_id, post_time) values(");
        query.append("\"" + post + "\",");
        query.append(hikeID + ", ");
        query.append(userID + ", ");
        String a = dateFormat.format(calendar.getTime());
        query.append("'" + dateFormat.format(calendar.getTime()) + "')");
        databaseConnector.updateData(query.toString());
        ResultSet resultSet = databaseConnector.getData("select ID from posts order by ID desc limit 1");
        try {
            if (resultSet.next()) {
                return resultSet.getInt(1);
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
    public int addComment(int userID, int postID, int hikeID, String comment, int privacyType) {
        StringBuilder query = new StringBuilder("insert into comments");
        query.append("(comment_text, hike_ID, user_ID, comment_time, privacy_type, post_ID)");
        String a = dateFormat.format(calendar.getTime());
        query.append("values ( '" + comment + "', " + hikeID + ", " + userID + ", '" + dateFormat.format(calendar.getTime()) + "'," + privacyType + ", ");
        if (privacyType == 1) {
            query.append("null)");
        } else if (privacyType == 2) {
            query.append("" + postID + ")");
        }
        databaseConnector.updateData(query.toString());
        ResultSet resultSet = databaseConnector.getData("select ID from comments order by ID desc limit 1");
        try {
            if (resultSet.next()) {
                return resultSet.getInt(1);
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

        ResultSet resultSet = databaseConnector.getData("select ID from post_likes order by ID desc limit 1");
        try {
            if (resultSet.next()) {
                return resultSet.getInt(1);
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
        if (likeExists(userID, commentID)) {
            databaseConnector.updateData("Delete from comment_likes where user_ID = " +
                    "\"" + userID + "\" AND comment_ID = " + "" + "\"" + commentID + "\";");
            return -1;
        }
        databaseConnector.updateData(query.toString());
        ResultSet resultSet = databaseConnector.getData("select ID from comment_likes order by ID desc limit 1");
        try {
            if (resultSet.next()) {
                return resultSet.getInt(1);
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
    public boolean likeExists(int userID, int commentID) {
        ResultSet rs = databaseConnector.getData("Select * from comment_likes where user_ID = " +
                "\"" + userID + "\" AND comment_ID = " + "" + "\"" + commentID + "\";");
        int nRows = 0;
        try {
            while(rs.next()){
                nRows++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nRows != 0;
    }

}
