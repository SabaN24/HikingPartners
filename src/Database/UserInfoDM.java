package Database;

import Models.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vache on 6/23/2017.
 */
public class UserInfoDM {
    private DatabaseConnector databaseConnector;
    private static UserInfoDM userInfoDM = null;

    private UserInfoDM(){
        databaseConnector = DatabaseConnector.getInstance();
    }

    public static UserInfoDM getInstance() {
        if (userInfoDM == null) {
            userInfoDM = new UserInfoDM();
        }
        return userInfoDM;
    }

    /**
     * Register User
     * @param facebookID
     * @param firstName
     * @param lastName
     * @param imgURL
     * @param birthDate
     * @param gender
     * @param email
     * @return newly registered user ID
     */
    public int registerUser(long facebookID, String firstName, String lastName, String imgURL, Date birthDate, String gender, String email){
        String query = "insert into users (facebook_id, first_name, last_name, img_url, birth_date, gender, email) values (?,?,?,?,?,?,?)";
        PreparedStatement registerUser = databaseConnector.getPreparedStatement(query);
        try {
            registerUser.setLong(1, facebookID);
            registerUser.setString(2, firstName);
            registerUser.setString(3, lastName);
            registerUser.setString(4, imgURL);
            registerUser.setDate(5, birthDate);
            registerUser.setString(6, gender);
            registerUser.setString(7, email);
            databaseConnector.updateDataWithPreparedStatement(registerUser);
            ResultSet resultSet = databaseConnector.getData("select ID from users order by ID desc limit 1");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Check if user is registered before or not
     * @param facebookID
     * @return boolean
     */
    public boolean isUserRegistered(long facebookID){
        String query = "select count(facebook_ID) from users where facebook_id = ?";
        PreparedStatement checkUser = databaseConnector.getPreparedStatement(query);
        try {
            checkUser.setLong(1, facebookID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(checkUser);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
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
     * @param facebookID
     * @param firstName
     * @param lastName
     * @param imgURL
     * @param birthDate
     * @param gender
     * @param email
     * @return updated user ID
     */
    public int updateUserInfo(long facebookID, String firstName, String lastName, String imgURL, Date birthDate, String gender, String email){
        String query = "update users set first_name = ?, last_name = ?, img_url = ?, birth_date = ?, gender = ?, email = ? where facebook_id = ?";
        PreparedStatement updateUserInfo = databaseConnector.getPreparedStatement(query);
        try {
            updateUserInfo.setString(1, firstName);
            updateUserInfo.setString(2, lastName);
            updateUserInfo.setString(3, imgURL);
            updateUserInfo.setDate(4, birthDate);
            updateUserInfo.setString(5, gender);
            updateUserInfo.setString(6, email);
            updateUserInfo.setLong(7, facebookID);
            databaseConnector.updateDataWithPreparedStatement(updateUserInfo);
            String returnIDQuery = "select ID from users where facebook_id = ?";
            PreparedStatement returnIDst = databaseConnector.getPreparedStatement(returnIDQuery);
            returnIDst.setLong(1, facebookID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(returnIDst);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Getting User by Facebook ID
     * @param facebookID
     * @return User depending on Facebook ID
     */
    public User getUserByFacebookkID(long facebookID) {
        String userReturnQuery = "select * from users where facebook_id = ?";
        PreparedStatement statement = databaseConnector.getPreparedStatement(userReturnQuery);
        try {
            statement.setLong(1, facebookID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(statement);
            if (resultSet.next()) {
                int userID = resultSet.getInt(1);
                long facebookId = resultSet.getLong(2);
                String firstName = resultSet.getString(3);
                String lastName = resultSet.getString(4);
                String imgUrl = resultSet.getString(5);
                Date birthDate = resultSet.getDate(6);
                String gender = resultSet.getString(7);
                String email = resultSet.getString(8);
                User user = new User(userID, firstName, lastName, imgUrl, facebookId, birthDate, gender, email);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
