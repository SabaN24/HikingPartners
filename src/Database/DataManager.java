package Database;

import java.util.*;
import Models.Comment;
import Models.User;
import Models.Hike.AboutModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataManager {


    DatabaseConnector databaseConnector;

    public DataManager() {
        databaseConnector = DatabaseConnector.getInstance();

    }


    /**
     * method uses dataConnector and gets resultset with query that
     * finds Hike with ID and puts infrmation into AboutModel class
     *
     * @param id which Hike user needs
     * @throws SQLException
     * @returns AboutModel thaat has relevant information
     */
    public AboutModel getAboutModel(int id) throws SQLException {

        AboutModel aboutModel = null;

        String hikeQuery = queryDecorator("hikes", "ID", "" + id);

        ResultSet hikeResultSet = databaseConnector.getData(hikeQuery);


        if (hikeResultSet.next()) {

            String name = hikeResultSet.getString(2);
            Date startDate = hikeResultSet.getDate(3);
            Date endDate = hikeResultSet.getDate(4);
            String description = hikeResultSet.getString(5);
            int maxPeople = hikeResultSet.getInt(6);
            List<Comment> comments = getComments("PUBLIC", "" + id);

            aboutModel = new AboutModel(id, name, description, startDate, endDate, maxPeople, comments);
        }


        return aboutModel;
    }


    /**
     * method gets comments with identififed type and ID
     * @param type (means PUBLIC or PRIVATE comments) if equals public than given parameter  ID means hike_id
     *             else given id menas post_ID;
     * @param id  for filering
     * @returns List of Comment classess
     * @throws SQLException
     */
    public List<Comment> getComments(String type, String id) throws SQLException {

        String commentQuery = "";
        List<Comment> comments = new ArrayList<>();

        if (type.equals("PUBLIC")) {
            commentQuery = queryDecorator("public_comments", "hike_ID", id);
        } else {
            commentQuery = queryDecorator("private_comments", "post_ID", id);
        }

        ResultSet commentsResultSet = databaseConnector.getData(commentQuery);

        while (commentsResultSet.next()) {

            int commentId = commentsResultSet.getInt(1);
            String comment = commentsResultSet.getString(2);
            int userId = commentsResultSet.getInt(4);

            /*
                user is not done yet so needs update !!!
             */
            User user = new User();
            Date date = commentsResultSet.getDate(5);
            int likeNum = commentsResultSet.getInt(6);

            Comment currComment = new Comment(commentId, comment, user, date, likeNum);
            comments.add(currComment);
        }

        return comments;
    }

    /**
     * method decorates given parameters for sql query syntax
     * @param Table  table from we get information
     * @param column column name
     * @param id     identificator in order to filter
     *               (selects rows thats' column equals identificator)
     * @return decorated query
     */
    private String queryDecorator(String Table, String column, String id) {
        String query = "SELECT * FROM " + Table + " WHERE " + column + " = " + "\"" + id + "\"" + " ;";
        return query;
    }


}
