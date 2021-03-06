package Database;

import Models.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by vache on 6/23/2017.
 */
public class UserInfoDM {
    private DatabaseConnector databaseConnector;
    private static UserInfoDM userInfoDM = null;
    private ReentrantLock userLock;

    /**
     * Private constructor of HikeSearchDM object (Singletone pattern)
     */
    private UserInfoDM(){
        userLock = new ReentrantLock();
        databaseConnector = DatabaseConnector.getInstance();
    }

    /**
     * getInstance method so that class is singletone.
     *
     * @return UserInfoDM object
     */
    public static UserInfoDM getInstance() {
        if (userInfoDM == null) {
            userInfoDM = new UserInfoDM();
        }
        return userInfoDM;
    }

    /**
     * Registers users with given parameters.
     * @param facebookID facebook id of user
     * @param firstName first name of user
     * @param lastName last name of user
     * @param profilePicURL url of profile picture of user
     * @param birthDate birth date of user
     * @param gender gender of user
     * @param email email of user
     * @param coverPicURL url of cover picture of user
     * @param facebookLink link to facebook of user
     * @return user id
     */
    public int registerUser(long facebookID, String firstName, String lastName, String profilePicURL, Date birthDate, String gender, String email, String coverPicURL, String facebookLink){
        String query = "insert into users (facebook_id, first_name, last_name, profile_picture_url, birth_date, gender, email,about_me_text,cover_picture_url, facebook_link ) values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement registerUser = databaseConnector.getPreparedStatement(query);
        try {
            registerUser.setLong(1, facebookID);
            registerUser.setString(2, firstName);
            registerUser.setString(3, lastName);
            registerUser.setString(4, profilePicURL);
            registerUser.setDate(5, birthDate);
            registerUser.setString(6, gender);
            registerUser.setString(7, email);
            registerUser.setString(8, "Hello! I am new Hiker!");
            registerUser.setString(9, coverPicURL);
            registerUser.setString(10, facebookLink);
            userLock.lock();
            databaseConnector.updateDataWithPreparedStatement(registerUser);
            int recentlyAdded = DatabaseHelper.getRecentlyAdded("users");
            return recentlyAdded;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            userLock.unlock();
        }
    }

    /**
     * Check if user is registered before or not
     * @param facebookID facebook id of user
     * @return boolean answer to search
     */
    public boolean isUserRegistered(long facebookID){
        String query = "select count(facebook_ID) count from users where facebook_id = ?";
        PreparedStatement checkUser = databaseConnector.getPreparedStatement(query);
        try {
            checkUser.setLong(1, facebookID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(checkUser);
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if(count == 0) return false;
                else if(count == 1) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updating past registered user info
     * @param facebookID facebook id of user
     * @param firstName first name of user
     * @param lastName last name of user
     * @param imgURL url of profile picture of user
     * @param birthDate birth date of user
     * @param gender gender of user
     * @param email email of user
     * @return updated user ID
     */
    public int updateUserInfo(long facebookID, String firstName, String lastName, String imgURL, Date birthDate, String gender, String email, String coverPicURL, String facebookLink){
        String query = "update users set first_name = ?, last_name = ?, profile_picture_url = ?, birth_date = ?, gender = ?, email = ?, about_me_text = ?, cover_picture_url = ?, facebook_link = ?  where facebook_id = ?";
        PreparedStatement updateUserInfo = databaseConnector.getPreparedStatement(query);
        try {
            updateUserInfo.setString(1, firstName);
            updateUserInfo.setString(2, lastName);
            updateUserInfo.setString(3, imgURL);
            updateUserInfo.setDate(4, birthDate);
            updateUserInfo.setString(5, gender);
            updateUserInfo.setString(6, email);
            updateUserInfo.setString(7, "Hello! I am new Hiker!");
            updateUserInfo.setString(8, coverPicURL);
            updateUserInfo.setString(9, facebookLink);
            updateUserInfo.setLong(10, facebookID);
            databaseConnector.updateDataWithPreparedStatement(updateUserInfo);
            String returnIDQuery = "select ID from users where facebook_id = ?";
            PreparedStatement returnIDst = databaseConnector.getPreparedStatement(returnIDQuery);
            returnIDst.setLong(1, facebookID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(returnIDst);
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Getting User by Facebook ID
     * @param facebookID  facebook id of user
     * @return User depending on Facebook ID
     */
    public User getUserByFacebookkID(long facebookID) {
        String userReturnQuery = "select * from users where facebook_id = ?";
        PreparedStatement statement = databaseConnector.getPreparedStatement(userReturnQuery);
        try {
            statement.setLong(1, facebookID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(statement);
            if (resultSet.next()) {
                int userID = resultSet.getInt("ID");
                long facebookId = resultSet.getLong("facebook_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String imgUrl = resultSet.getString("profile_picture_url");
                Date birthDate = resultSet.getDate("birth_date");
                String gender = resultSet.getString("gender");
                String email = resultSet.getString("email");
                String aboutMe = resultSet.getString("about_me_text");
                String coverPictureAddress = resultSet.getString("cover_picture_url");
                String facebookLink = resultSet.getString("facebook_link");

                User user = new User(userID, firstName, lastName, imgUrl, facebookId, birthDate, gender, email, aboutMe, coverPictureAddress , facebookLink);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Getting User by ID
     * @param ID id of user
     * @return User depending on ID
     */
    public User getUserByID(int ID) {
        String userReturnQuery = "select * from users where ID = ?";
        PreparedStatement statement = databaseConnector.getPreparedStatement(userReturnQuery);
        try {
            statement.setInt(1, ID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(statement);
            if (resultSet.next()) {
                int userID = resultSet.getInt("ID");
                long facebookId = resultSet.getLong("facebook_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String imgUrl = resultSet.getString("profile_picture_url");
                Date birthDate = resultSet.getDate("birth_date");
                String gender = resultSet.getString("gender");
                String email = resultSet.getString("email");
                String aboutMe = resultSet.getString("about_me_text");
                String coverPictureAddress = resultSet.getString("cover_picture_url");
                String facebookLink = resultSet.getString("facebook_link");
                User user = new User(userID, firstName, lastName, imgUrl, facebookId, birthDate, gender, email, aboutMe, coverPictureAddress , facebookLink);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Returns user object with given name.
     * @param searchedName name of user
     * @return user object
     */
    public User getUserByName(String searchedName){

        String query = "select id from users where concat(concat(first_name, \" \"), last_name) like ?";
        PreparedStatement pst = databaseConnector.getPreparedStatement(query);

        try {
            pst.setString(1, "%" + searchedName + "%");
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(pst);
            if (resultSet.next()) {
               int ID = resultSet.getInt(1);
               return getUserByID(ID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Updates "about-me-text" of user with given id.
     * @param userId id of user
     */
    public void updateAboutMeInfo(int userId, String text){
        String query = "update users set about_me_text = ? where id = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseConnector.updateDataWithPreparedStatement(preparedStatement);
    }

}
