package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Nodo on 6/27/2017.
 */
public class HikeDM {



    private DatabaseConnector databaseConnector;

    public static final String ATTR = "HikeDM";

    private static HikeDM hikeDM = null;

    private HikeDM() {
        databaseConnector = DatabaseConnector.getInstance();
    }

    public static HikeDM getInstance() {
        if (hikeDM == null) {
            hikeDM = new HikeDM();
        }
        return hikeDM;
    }

    /**
     * void registers Hike with these parameters.
     * @param hikeName
     * @param startDate
     * @param endDate
     * @param description
     * @param maxPeople
     * @param creatorId
     * @return lately added hike ID
     */
    public int addNewHike(String hikeName, Date startDate, Date endDate, String description, int maxPeople, int creatorId){
        String query = "insert into hikes (hike_name, start_date , end_date, description, max_people) values (?,?,?,?,?)";
        PreparedStatement registerHike = databaseConnector.getPreparedStatement(query);
        try {

            registerHike.setString(1, hikeName);
            registerHike.setDate(2, new java.sql.Date(startDate.getTime()));
            registerHike.setDate(3, new java.sql.Date(endDate.getTime()));
            registerHike.setString(4, description);
            registerHike.setInt(5, maxPeople);

            databaseConnector.updateDataWithPreparedStatement(registerHike);
            ResultSet resultSet = databaseConnector.getData("select ID from hikes order by ID desc limit 1");
            if (resultSet.next()) {
                //added creator into hike as as creator
                int hikeId  = resultSet.getInt(1);
                addUserToHike(hikeId, creatorId, 1 );
                return hikeId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;

    }


    /**
     * registers user to hike with these params
     * @param hikeId
     * @param userId
     * @param roleId
     */
    public void addUserToHike(int hikeId, int userId, int roleId){
        String query = "insert into hike_to_user (hike_ID ,user_ID,role_ID) values (?,?,?)";
        PreparedStatement registerUserIntoHike = databaseConnector.getPreparedStatement(query);

        try {
            registerUserIntoHike.setInt(1, hikeId);
            registerUserIntoHike.setInt(2, userId);
            registerUserIntoHike.setInt(3,roleId);


            databaseConnector.updateDataWithPreparedStatement(registerUserIntoHike);



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds new request to database.
     * @param senderId id of user who sent request
     * @param receiverId id of creator of hike
     * @param hikeId id of hike, in which user wishes to be added
     * @return id of newly created request
     */
    public int addRequest(int senderId, int receiverId, int hikeId) {
        String query = "insert into requests (sender_ID, receiver_ID, hike_ID) values (?,?,?)";
        PreparedStatement addRequest = databaseConnector.getPreparedStatement(query);
        try {
            addRequest.setInt(1, senderId);
            addRequest.setInt(2, receiverId);
            addRequest.setInt(3, hikeId);
            databaseConnector.updateDataWithPreparedStatement(addRequest);
            ResultSet resultSet = databaseConnector.getData("select ID from requests order by ID desc limit 1");
            if (resultSet.next()) {
                int requestId = resultSet.getInt("ID");
                return requestId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
