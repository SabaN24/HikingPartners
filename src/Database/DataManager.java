package Database;

import java.util.*;

import Models.Comment;
import Models.User;
import Models.Hike.AboutModel;
import Models.Hike.DefaultModel;
import Models.Photo;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataManager {


    private DatabaseConnector databaseConnector;

    public static final String ATTR = "DatabaseManager";

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
    public AboutModel getAboutModel(int id) {

        AboutModel aboutModel = null;

        String hikeQuery = constructQuery("hikes", "ID", "" + id);

        ResultSet hikeResultSet = databaseConnector.getData(hikeQuery);


        try {
            while (hikeResultSet.next()) {

                String name = hikeResultSet.getString(2);
                Date startDate = hikeResultSet.getDate(3);
                Date endDate = hikeResultSet.getDate(4);
                String description = hikeResultSet.getString(5);
                int maxPeople = hikeResultSet.getInt(6);
                List<Comment> comments = getComments("PUBLIC", "" + id);

                aboutModel = new AboutModel(id, name, description, startDate, endDate, maxPeople, comments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return aboutModel;
    }


    /**
     * method gets comments with identififed type and ID
     *
     * @param type (means PUBLIC or PRIVATE comments) if equals public than given parameter  ID means hike_id
     *             else given id menas post_ID;
     * @param id   for filering
     * @throws SQLException
     * @returns List of Comment classess
     */
    public List<Comment> getComments(String type, String id) {

        String commentQuery = "";
        List<Comment> comments = new ArrayList<>();

        if (type.equals("PUBLIC")) {
            commentQuery = constructQuery("public_comments", "hike_ID", id);
        } else {
            commentQuery = constructQuery("private_comments", "post_ID", id);
        }

        ResultSet commentsResultSet = databaseConnector.getData(commentQuery);

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Returns information needed for DefaultModel class.
     * @param hikeId id of demanded hike
     * @return DefaultModel class built from information about given hike
     */
    public DefaultModel getDefaultModel(int hikeId) {
        DefaultModel.User creator = getCreator(hikeId);
        List<Photo> coverPhotos = getCoverPhotos(hikeId);
        DefaultModel defaultModel = new DefaultModel(creator, coverPhotos);
        return defaultModel;
    }

    /**
     * Gets information about creator of given hike from database, using callProcedure
     * method of databaseConnnector class which calls given procedure in database.
     * @param hikeId id of demanded hike
     * @return DefaultMode.User class object built from information of creator of given hike
     */
    private DefaultModel.User getCreator(int hikeId) {
        ResultSet rs = databaseConnector.callProcedure("get_creator_info", "" + hikeId);
        DefaultModel.User creator = null;
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String imgUrl = rs.getString(4);
                creator = new DefaultModel.User(id, firstName, lastName, imgUrl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creator;
    }

    /**
     * Gets cover photos of given hike from database, using callProcedure
     * method of databaseConnnector class which calls given procedure in database.
     * @param hikeId id of demanded hike
     * @return List<Photo> object built from cover photos of given hike
     */
    private List<Photo> getCoverPhotos(int hikeId){
        ResultSet rs = databaseConnector.callProcedure("get_cover_photos", "" + hikeId);
        List<Photo> coverPhotos = new ArrayList<>();
        try {
            while(rs.next()){
                int id = rs.getInt(1);
                String url = rs.getString(2);
                String name = rs.getString(3);
                String description = rs.getString(4);
                Photo photo = new Photo(id, url, name, description);
                coverPhotos.add(photo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coverPhotos;
    }

    /**
     * method decorates given parameters for sql query syntax
     *
     * @param Table  table from we get information
     * @param column column name
     * @param id     identificator in order to filter
     *               (selects rows thats' column equals identificator)
     * @return decorated query
     */
    private String constructQuery(String Table, String column, String id) {
        String query = "SELECT * FROM " + Table + " WHERE " + column + " = " + "\"" + id + "\";";
        return query;
    }
}
