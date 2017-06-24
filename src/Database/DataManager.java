package Database;

import java.sql.Time;
import java.text.DateFormat;
import java.util.*;

import Models.*;
import Models.Hike.AboutModel;
import Models.Hike.DefaultModel;
import Models.Hike.HikeInfo;

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

    public static DataManager getInstance() {
        if (dm == null) {
            dm = new DataManager();
        }
        return dm;
    }

    /**
     * Returns list of all hikes that are in database.
     * @return all hikes as List<HikeInfo>
     */
    public List<HikeInfo> getAllHikes() {
        List<HikeInfo> hikes = new ArrayList<>();
        ResultSet allHikes = databaseConnector.getData("Select * from hikes;");
        try {
            while (allHikes.next()) {
                int id = allHikes.getInt("ID");
                Date startDate = allHikes.getDate("start_date");
                Date endDate = allHikes.getDate("end_date");
                String descrption = allHikes.getString("description");
                int maxPeople = allHikes.getInt("max_people");
                ResultSet joinedPeopleRS = databaseConnector.callProcedure("get_joined_people",
                        Arrays.asList("" + id));
                int joinedPeople = 0;
                if (joinedPeopleRS.next()) {
                    joinedPeople = joinedPeopleRS.getInt("count");
                }
                DefaultModel defaultModel = getDefaultModel(id);
                HikeInfo current = new HikeInfo(id, defaultModel.getName(), defaultModel.getCreator(),
                        defaultModel.getCoverPhotos(), maxPeople, joinedPeople, startDate, endDate, descrption);
                hikes.add(current);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hikes;
    }

    /**
     * method uses dataConnector and gets resultset with query that
     * finds Hike with ID and puts infrmation into AboutModel class
     *
     * @param id     which Hike user needs
     * @param userID which user is trying to access this hike
     * @throws SQLException
     * @returns AboutModel thaat has relevant information
     */
    public AboutModel getAboutModel(int id, int userID) {
        AboutModel aboutModel = null;
        String hikeQuery = constructQuery("hikes", "ID", "" + id);
        ResultSet hikeResultSet = databaseConnector.getData(hikeQuery);
        try {
            while (hikeResultSet.next()) {
                Date startDate = hikeResultSet.getDate("start_date");
                Date endDate = hikeResultSet.getDate("end_date");
                String description = hikeResultSet.getString("desxription");
                int maxPeople = hikeResultSet.getInt("max_people");
                List<Comment> comments = getComments("-1", "" + id, 1, userID);
                aboutModel = new AboutModel(description, startDate, endDate, maxPeople, comments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aboutModel;
    }

    /**
     * Returns if user has liked this comment or not.
     */
    private boolean userHasLiked(List<Like> likes, int userID, int commentID) {
        for (Like like : likes) {
            if (like.getCommentID() == commentID && like.getUserID() == userID) {
                return true;
            }
        }
        return false;
    }


    /**
     * Gets comments of given post.
     *
     * @param postID      id of required post
     * @param hikeID      id of hike, on which this post is written
     * @param commentType type of comment (private or public)
     * @return comments on given post as ArrayList
     */
    public List<Comment> getComments(String postID, String hikeID, int commentType, int userID) {
        List<Comment> comments = new ArrayList<>();
        List<Like> likes = new ArrayList<>();
        ResultSet likesSet;
        likesSet = databaseConnector.callProcedure("get_post_comments_likes", Arrays.asList(postID, hikeID, "" + commentType));
        likes = parseLikes(likesSet);
        ResultSet commentsResultSet;
        if (commentType == 1) {
            commentsResultSet = databaseConnector.callProcedure("get_public_comments", Arrays.asList(hikeID));
        } else {
            commentsResultSet = databaseConnector.callProcedure("get_private_comments", Arrays.asList(postID));
        }
        try {
            while (commentsResultSet.next()) {
                int commentId = commentsResultSet.getInt("ID");
                String comment = commentsResultSet.getString("comment_text");
                int authorID = commentsResultSet.getInt("user_ID");
                MiniUser author = getUserById(authorID);

                Date date = (Date) commentsResultSet.getObject("comment_time");

                ResultSet likeResultSet = databaseConnector.callProcedure("get_comment_likes", Arrays.asList("" + commentId));
                int likeNum = 0;
                if (likeResultSet.next()) {
                    likeNum = likeResultSet.getInt("count");
                }
                Comment currComment = new Comment(commentId, comment, -1, author, date, likeNum, userHasLiked(likes, userID, commentId));
                comments.add(currComment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    private List<Like> parseLikes(ResultSet likesSet) {
        List<Like> likes = new ArrayList<>();
        try {
            while (likesSet.next()) {
                int userID = likesSet.getInt("user_id");
                int commentID = likesSet.getInt("comment_ID");
                likes.add(new Like(commentID, userID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return likes;
    }

    /**
     * Gets information from database about user given user's id.
     *
     * @param userID id of required user
     * @return MiniUser class built on information from database
     */
    public MiniUser getUserById(int userID) {
        String query = constructQuery("users", "id", "" + userID);
        ResultSet rs = databaseConnector.getData(query);
        MiniUser user = createUserFromResultSet(rs);
        return user;
    }

    /**
     * Returns information needed for DefaultModel class.
     *
     * @param hikeId id of demanded hike
     * @return DefaultModel class built from information about given hike
     */
    public DefaultModel getDefaultModel(int hikeId) {
        String hikeQuery = constructQuery("hikes", "ID", "" + hikeId);
        ResultSet hikeResultSet = databaseConnector.getData(hikeQuery);
        String name = null;
        try {
            while (hikeResultSet.next()) {
                name = hikeResultSet.getString("hike_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MiniUser creator = getCreator(hikeId);
        List<Photo> coverPhotos = getCoverPhotos(hikeId);
        DefaultModel defaultModel = new DefaultModel(hikeId, name, creator, coverPhotos);
        return defaultModel;
    }

    /**
     * Returns posts of given hike as list.
     *
     * @param hikeID id of desired hike
     * @param userID id of view requesting user
     * @return list of posts
     */
    public List<Post> getPosts(int hikeID, int userID) {
        ResultSet rs = databaseConnector.getData("Select * from posts where hike_ID = " + hikeID + ";");
        List<Post> posts = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String text = rs.getString("post_text");
                int authorID = rs.getInt("user_ID");
                Date postDate = (Date) rs.getObject("post_time");
                MiniUser user = getUserById(authorID);
                List<Comment> comments = getComments("" + id, "" + hikeID, 2, userID);
                ResultSet likesSet = databaseConnector.callProcedure("get_post_likes", Arrays.asList("" + id));
                int likes = 0;
                if (likesSet.next()) {
                    likes = likesSet.getInt("count");
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
     *
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
     *
     * @param rs data which needs to be processed
     * @return MiniUser object
     */
    private MiniUser createUserFromResultSet(ResultSet rs) {
        MiniUser creator = null;
        try {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String imgUrl = rs.getString("img_url");
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
     *
     * @param hikeId id of demanded hike
     * @return List<Photo> object built from cover photos of given hike
     */
    private List<Photo> getCoverPhotos(int hikeId) {
        ResultSet rs = databaseConnector.callProcedure("get_cover_photos", Arrays.asList("" + hikeId));
        List<Photo> coverPhotos = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String url = rs.getString("img_url");
                String name = rs.getString("loc_name");
                String description = rs.getString("description");
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
