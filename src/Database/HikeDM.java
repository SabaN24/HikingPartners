package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import Models.Hike.DefaultModel;
import Models.Hike.HikeInfo;
import Models.Request;
import Models.User;

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
     *
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
     * Returns whole information about hike with given id
     *
     * @param hikeId id of desired hike
     * @return HikeInfo object built from information of given hike
     */
    public HikeInfo getHikeById(int hikeId) {
        String query = "Select * from hikes where id = " + hikeId + ";";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        ResultSet hikeRS = databaseConnector.getDataWithPreparedStatement(preparedStatement);
        try {
            while (hikeRS.next()) {
                int id = hikeRS.getInt("ID");
                Date startDate = (Date) hikeRS.getObject("start_date");
                Date endDate = (Date) hikeRS.getObject("end_date");
                String description = hikeRS.getString("description");
                int maxPeople = hikeRS.getInt("max_people");
                ResultSet joinedPeopleRS = databaseConnector.callProcedure("get_joined_people",
                        Arrays.asList("" + id));
                int joinedPeople = 0;
                if (joinedPeopleRS.next()) {
                    joinedPeople = joinedPeopleRS.getInt("count");
                }
                DefaultModel defaultModel = MainDM.getInstance().getDefaultModel(id);
                HikeInfo hikeInfo = new HikeInfo(id, defaultModel.getName(), defaultModel.getCreator(),
                        defaultModel.getCoverPhotos(), maxPeople, joinedPeople, startDate, endDate, description);
                return hikeInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    /**
     * Returns List of all requests received by given user.
     *
     * @param userId id of user whose requests need to be fetched
     * @return list of requests
     */
    public List<Request> getRequestsOfUser(int userId) {
        String query = "SELECT * FROM REQUESTS WHERE receiver_ID = ?;";
        List<Request> requests = new ArrayList<>();
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, userId);
            ResultSet rs = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            while (rs.next()) {
                int id = rs.getInt("ID");
                int senderId = rs.getInt("sender_ID");
                int receiverId = rs.getInt("receiver_ID");
                int hikeId = rs.getInt("hike_ID");
                UserInfoDM userInfoDM = UserInfoDM.getInstance();
                User sender = userInfoDM.getUserByID(senderId);
                User receiver = userInfoDM.getUserByID(receiverId);
                HikeInfo hike = getHikeById(hikeId);
                Request request = new Request(id, sender, receiver, hike);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * Responds to request with given message.
     *
     * @param requestId id of request to be responded
     * @param response  type of response (accept or reject)
     */
    public void respondToRequest(int requestId, String response) {
        databaseConnector.callProcedure("request_response", Arrays.asList("" + requestId, "\"" + response + "\""));
    }

    /**
     * Gets ids of requested hikes for given user.
     * @param userId user whose requested hikes need to be fetcher
     * @return ids as list
     */
    public List<Integer> getRequestedHikeIds(int userId){
        String query = "select id from hikes where id in (select hike_ID from requests where sender_ID = ?)";
        PreparedStatement st = databaseConnector.getPreparedStatement(query);
        List<Integer> res = new ArrayList<>();
        try {
            st.setInt(1, userId);
            ResultSet rs = databaseConnector.getDataWithPreparedStatement(st);
            if (rs.next()) {
                res.add(rs.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Adds cover photo to given hike with given description
     * @param description description of cover photo
     * @param newFilePath path of photo
     * @param hikeID id of hike
     * @return id of new hike photo
     */
    public int addCoverPhoto(String description, String newFilePath, int hikeID) {
        String query = "insert into cover_photos (description, hike_ID, img_url) values (?,?,?)";
        PreparedStatement coverPhotoStatement = databaseConnector.getPreparedStatement(query);
        try {
            coverPhotoStatement.setString(1, description);
            coverPhotoStatement.setInt(2, hikeID);
            coverPhotoStatement.setString(3, newFilePath);
            databaseConnector.updateDataWithPreparedStatement(coverPhotoStatement);
            ResultSet resultSet = databaseConnector.getData("select ID from cover_photos order by ID desc limit 1");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * Updates cover photo with given id
     * @param description description of cover photo
     * @param newFilePath path of photo
     * @param coverID id of photo
     */
    public void updateCoverPhoto(int coverID, String description, String newFilePath) {
        if(newFilePath == null){
            String query = "update cover_photos set description = ? where ID = ?";
            PreparedStatement updateCoverStatement = databaseConnector.getPreparedStatement(query);
            try {
                updateCoverStatement.setString(1, description);
                updateCoverStatement.setInt(2, coverID);
                databaseConnector.updateDataWithPreparedStatement(updateCoverStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            String query = "update cover_photos set description = ?, newFilePath = ? where ID = ?";
            PreparedStatement updateCoverStatement = databaseConnector.getPreparedStatement(query);
            try {
                updateCoverStatement.setString(1, description);
                updateCoverStatement.setString(2, newFilePath);
                updateCoverStatement.setInt(3, coverID);
                databaseConnector.updateDataWithPreparedStatement(updateCoverStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates description of hike with given id with given text.
     * @param id id of hike description of which needs to be updated
     * @param text new description of hike
     */
    public void updateDescription(int id, String text){
        String query = "update hikes set description = ? where id = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseConnector.updateDataWithPreparedStatement(preparedStatement);
    }

    /**
     * Updates name of hike with given id with given text.
     * @param id id of hike description of which needs to be updated
     * @param text new name of hike
     */
    public void updateName(int id, String text){
        String query = "update hikes set hike_name = ? where id = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseConnector.updateDataWithPreparedStatement(preparedStatement);
    }

}
