package Database;

import Models.*;
import Models.Hike.AboutModel;
import Models.Hike.DefaultModel;
import Models.Hike.HikeInfo;
import Models.Hike.HikeInfoExtended;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class MainDM {


    private DatabaseConnector databaseConnector;

    public static final String ATTR = "DatabaseManager";

    private static MainDM dm = null;

    /**
     * Private constructor of HikeSearchDM object (Singletone pattern)
     */
    private MainDM() {
        databaseConnector = DatabaseConnector.getInstance();
    }

    /**
     * getInstance method so that class is singletone.
     *
     * @return MainDM object
     */
    public static MainDM getInstance() {
        if (dm == null) {
            dm = new MainDM();
        }
        return dm;
    }

    /**
     * Returns list of all hikes that are in database.
     * @return all hikes as List<HikeInfo>
     */
    public List<HikeInfo> getHikes(int userId) {
        List<HikeInfo> hikes = new ArrayList<>();
        ResultSet allHikes = null;
        if(userId == -1){
            allHikes = databaseConnector.getData("Select * from hikes;");
        } else {
           allHikes = databaseConnector.callProcedure("get_hikes_by_user", Arrays.asList("" + userId));
        }
        try {
            while (allHikes.next()) {
                int id = allHikes.getInt("ID");
                Date startDate = (Date)allHikes.getObject("start_date");
                Date endDate = (Date)allHikes.getObject("end_date");
                String description = allHikes.getString("description");
                int maxPeople = allHikes.getInt("max_people");
                ResultSet joinedPeopleRS = databaseConnector.callProcedure("get_joined_people",
                        Arrays.asList("" + id));
                int joinedPeople = 0;
                if (joinedPeopleRS.next()) {
                    joinedPeople = joinedPeopleRS.getInt("count");
                }
                DefaultModel defaultModel = getDefaultModel(id);
                HikeInfo current = new HikeInfo(id, defaultModel.getName(), defaultModel.getCreator(),
                        defaultModel.getCoverPhotos(), maxPeople, joinedPeople, startDate, endDate, description);
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
                Date startDate = (Date)hikeResultSet.getObject("start_date");
                Date endDate = (Date)hikeResultSet.getObject("end_date");
                String description = hikeResultSet.getString("description");
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
                User author = getUserById(authorID);

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

    /**
     * Parse list of likes from given resultset.
     * @param likesSet resultset returned by database
     * @return list of likes
     */
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
    public User getUserById(int userID) {
        String query = constructQuery("users", "id", "" + userID);
        ResultSet rs = databaseConnector.getData(query);
        User user = createUserFromResultSet(rs);
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
        User creator = getCreator(hikeId);
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
                String link = rs.getString("link");
                int authorID = rs.getInt("user_ID");
                Date postDate = (Date) rs.getObject("post_time");
                User user = getUserById(authorID);
                List<Comment> comments = getComments("" + id, "" + hikeID, 2, userID);
                ResultSet likesSet = databaseConnector.callProcedure("get_post_likes", Arrays.asList("" + id));
                int likes = 0;
                if (likesSet.next()) {
                    likes = likesSet.getInt("count");
                }
                int photoID = rs.getInt("photo_ID");
                Photo photo = GalleryDM.getInstance().getGalleryPhoto(photoID);
                Post p = new Post(id, text, link, user, postDate, comments, likes, photo);
                posts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Returns hike members depending for given hike
     * @param hikeID id of hike
     * @return Members of given hike as list
     */
    public List<Member> getHikeMembers(int hikeID){
        ResultSet rs = databaseConnector.callProcedure("get_hike_members", Arrays.asList("" + hikeID));
        List<Member> res = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("ID");
                long fbID = rs.getLong("facebook_ID");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String imgUrl = rs.getString("profile_picture_url");
                Date birthdate = (Date)rs.getObject("birth_date");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String aboutMe = rs.getString("about_me_text");
                String coverPicUrl = rs.getString("cover_picture_url");
                String fbLink = rs.getString("facebook_link");
                int roleID = rs.getInt("role_ID");
                res.add(new Member(id, firstName, lastName, imgUrl, fbID, birthdate, gender, email, aboutMe, coverPicUrl, fbLink, roleID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Gets information about creator of given hike from database, using callProcedure
     * method of databaseConnnector class which calls given procedure in database.
     *
     * @param hikeId id of demanded hike
     * @return DefaultMode.User class object built from information of creator of given hike
     */
    public User getCreator(int hikeId) {
        ResultSet rs = databaseConnector.callProcedure("get_creator_info", Arrays.asList("" + hikeId));
        User creator = createUserFromResultSet(rs);
        return creator;
    }

    /**
     * Gets list of notifications for given user
     * @param userID id of user
     * @return list of all notifications for given user
     */
    public List<Notification> getNotifications(int userID){
        List<Notification> res = new ArrayList<>();
        String query = "select * from notifications where user_ID = ?";
        PreparedStatement statement = databaseConnector.getPreparedStatement(query);
        try {
            statement.setInt(1, userID);
            ResultSet rs = databaseConnector.getDataWithPreparedStatement(statement);
            while(rs.next()){
                Integer ID = rs.getInt("ID");
                Date time = (Date) rs.getObject("time");
                Integer typeID = rs.getInt("type_ID");
                Integer postID = (Integer) rs.getObject("post_ID");
                Integer fromUserID = rs.getInt("from_user_ID");
                User fromUser = UserInfoDM.getInstance().getUserByID(fromUserID);
                Integer requestID = (Integer) rs.getObject("request_ID");
                Integer hikeID = (Integer) rs.getObject("hike_ID");
                String hikeName = (String) rs.getObject("hike_name");
                Integer seen = rs.getInt("seen");
                Notification notification = new Notification(ID, userID, time, typeID, postID, fromUser, requestID, hikeID, hikeName, seen);
                res.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Marks notification as seen
     * @param notID id of notification
     */
    public void seeNotification(int notID){
        String quest = "update notifications set seen = 1 where ID = ?;";
        PreparedStatement statement = databaseConnector.getPreparedStatement(quest);
        try {
            statement.setInt(1, notID);
            databaseConnector.updateDataWithPreparedStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds MiniUser object from given resultset.
     *
     * @param rs data which needs to be processed
     * @return MiniUser object
     */
    private User createUserFromResultSet(ResultSet rs) {
        User user = null;
        try {
            while (rs.next()) {
                int id = rs.getInt("ID");
                long fbID = rs.getLong("facebook_ID");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String imgUrl = rs.getString("profile_picture_url");
                Date birthdate = (Date)rs.getObject("birth_date");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String aboutMe = rs.getString("about_me_text");
                String coverPicUrl = rs.getString("cover_picture_url");
                String fbLink = rs.getString("facebook_link");
                user = new User(id, firstName, lastName, imgUrl, fbID,  birthdate, gender, email, aboutMe, coverPicUrl, fbLink);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    /**
     * Gets cover photos of given hike from database, using callProcedure
     * method of databaseConnnector class which calls given procedure in database.
     *
     * @param hikeId id of demanded hike
     * @return List<Photo> object built from cover photos of given hike
     */
    public List<Photo> getCoverPhotos(int hikeId) {
        ResultSet rs = databaseConnector.callProcedure("get_cover_photos", Arrays.asList("" + hikeId));
        List<Photo> coverPhotos = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String url = rs.getString("img_url");
                String description = rs.getString("description");
                Photo photo = new Photo(id, url, description);
                coverPhotos.add(photo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coverPhotos;
    }


    /**
     * Creates sql query with given parameters.
     *
     * @param table  table from we get information
     * @param column column name
     * @param id column value
     * @return decorated query
     */
    private String constructQuery(String table, String column, String id) {
        String query = "SELECT * FROM " + table + " WHERE " + column + " = " + "\"" + id + "\";";
        return query;
    }

    /**
     * Returns homepage info for given hike.
     * @param userID id of user who tries to access info
     * @return info about hike
     */
    public List<HikeInfoExtended> getHomePageInfo(int userID) {
        List<HikeInfoExtended> result = new ArrayList<>();
        ResultSet rs = databaseConnector.callProcedure("get_home_page_info", Arrays.asList("" + userID));
        try {
            while (rs.next()) {
                int id = rs.getInt("ID");
                Date startDate = (Date)rs.getObject("start_date");
                Date endDate = (Date)rs.getObject("end_date");
                String description = rs.getString("description");
                int maxPeople = rs.getInt("max_people");
                int userStatus = rs.getInt("user_status");
                ResultSet joinedPeopleRS = databaseConnector.callProcedure("get_joined_people",
                        Arrays.asList("" + id));
                int joinedPeople = 0;
                if (joinedPeopleRS.next()) {
                    joinedPeople = joinedPeopleRS.getInt("count");
                }
                DefaultModel defaultModel = getDefaultModel(id);
                HikeInfoExtended elem = new HikeInfoExtended(id, defaultModel.getName(), defaultModel.getCreator(),
                        defaultModel.getCoverPhotos(), maxPeople, joinedPeople, startDate, endDate, description, userStatus);
                result.add(elem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
