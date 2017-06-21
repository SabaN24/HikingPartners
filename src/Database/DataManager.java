package Database;

import java.sql.Time;
import java.text.DateFormat;
import java.util.*;

import Models.*;
import Models.Hike.AboutModel;
import Models.Hike.DefaultModel;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataManager {


    private DatabaseConnector databaseConnector;

    public static final String ATTR = "DatabaseManager";

    private static DataManager dm = null;

    private DataManager() {
        databaseConnector = DatabaseConnector.getInstance();
    }

    public static DataManager getInstance(){
        if(dm == null) {
            dm = new DataManager();
        }
        return dm;
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
                List<Comment> comments = getComments("", "" + id, 1);
                aboutModel = new AboutModel(id, name, description, startDate, endDate, maxPeople, comments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aboutModel;
    }


    /**
     * Gets comments of given post.
     * @param postID id of required post
     * @param hikeID id of hike, on which this post is written
     * @param commentType type of comment (private or public)
     * @return comments on given post as ArrayList
     */
    public List<Comment> getComments(String postID, String hikeID, int commentType) {
        List<Comment> comments = new ArrayList<>();
        ResultSet commentsResultSet;
        if (commentType == 1) {
            commentsResultSet = databaseConnector.callProcedure("get_public_comments", Arrays.asList(hikeID));
        } else {
            commentsResultSet = databaseConnector.callProcedure("get_private_comments", Arrays.asList(postID));
        }
        try {
            while (commentsResultSet.next()) {
                int commentId = commentsResultSet.getInt(1);
                String comment = commentsResultSet.getString(2);
                int userID = commentsResultSet.getInt(3);
                MiniUser author = getUserById(userID);

                Date date = (Date)commentsResultSet.getObject(4);

                ResultSet likeResultSet = databaseConnector.callProcedure("get_comment_likes", Arrays.asList("" + commentId));
                int likeNum = 0;
                if(likeResultSet.next()){
                    likeNum = likeResultSet.getInt(1);
                }
                Comment currComment = new Comment(commentId, comment, -1, author, date, likeNum);
                comments.add(currComment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    /**
     * Gets information from database about user given user's id.
     * @param userID id of required user
     * @return MiniUser class built on information from database
     */
    public MiniUser getUserById(int userID){
        String query = constructQuery("users", "id", "" + userID);
        ResultSet rs = databaseConnector.getData(query);
        MiniUser user = createUserFromResultSet(rs);
        return user;
    }

    /**
     * Returns information needed for DefaultModel class.
     * @param hikeId id of demanded hike
     * @return DefaultModel class built from information about given hike
     */
    public DefaultModel getDefaultModel(int hikeId) {
        MiniUser creator = getCreator(hikeId);
        List<Photo> coverPhotos = getCoverPhotos(hikeId);
        DefaultModel defaultModel = new DefaultModel(creator, coverPhotos);
        return defaultModel;
    }

    /**
     * Returns posts of given hike as list.
     * @param hikeID id of desired hike
     * @return list of posts
     */
    public List<Post> getPosts(int hikeID) {
        ResultSet rs = databaseConnector.getData("Select * from posts where hike_ID = " + hikeID + ";");
        List<Post> posts = new ArrayList<>();
        try {
            while(rs.next()){
                int id = rs.getInt(1);
                String text = rs.getString(2);
                int userID = rs.getInt(4);
                Date postDate = (Date)rs.getObject(5);
                MiniUser user = getUserById(userID);
                List<Comment> commentsList = getComments("" + id, "" + hikeID, 2);
                ArrayList<Comment> comments = new ArrayList<>(commentsList);
                ResultSet likesSet = databaseConnector.callProcedure("get_post_likes", Arrays.asList("" + id));
                int likes = 0;
                if(likesSet.next()) {
                    likes = likesSet.getInt(1);
                }
                Post p = new Post(id, text, user, postDate, comments, likes);
                posts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Gets information about creator of given hike from database, using callProcedure
     * method of databaseConnnector class which calls given procedure in database.
     * @param hikeId id of demanded hike
     * @return DefaultMode.User class object built from information of creator of given hike
     */
    private MiniUser getCreator(int hikeId) {
        ResultSet rs = databaseConnector.callProcedure("get_creator_info", Arrays.asList("" + hikeId));
        MiniUser creator = createUserFromResultSet(rs);
        return creator;
    }

    /**
     * Builds MiniUser object from given resultset.
     * @param rs data which needs to be processed
     * @return MiniUser object
     */
    private MiniUser createUserFromResultSet(ResultSet rs){
        MiniUser creator = null;
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String imgUrl = rs.getString(4);
                creator = new MiniUser(id, firstName, lastName, imgUrl);
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
        ResultSet rs = databaseConnector.callProcedure("get_cover_photos", Arrays.asList("" + hikeId));
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
