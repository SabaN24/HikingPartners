package Database;

import Models.Hike.HikeInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vache on 7/7/2017.
 */
public class HikeSearchDM {
    private DatabaseConnector databaseConnector;
    private HikeDM hikeDM;
    private static HikeSearchDM hikeSearchDM = null;

    /**
     * Private constructor of HikeSearchDM object (Singletone pattern)
     */
    private HikeSearchDM() {
        databaseConnector = DatabaseConnector.getInstance();
        hikeDM = HikeDM.getInstance();
    }

    /**
     * getInstance method so that class is singletone.
     *
     * @return HikeSearchDM object
     */
    public static HikeSearchDM getInstance() {
        if (hikeSearchDM == null) {
            hikeSearchDM = new HikeSearchDM();
        }
        return hikeSearchDM;
    }

    /**
     * Gets list of hikes which match location parameter given in search.
     * @param latFrom starting latitude of search
     * @param latTo ending latitude of search
     * @param lngFrom starting longitude of search
     * @param lngTo ending longitude of search
     * @return list of hikes
     */
    public List<HikeInfo> getSearchedHikesByLocation(Double latFrom, Double latTo, Double lngFrom, Double lngTo) {
        Set<Integer> searchedHikeIds = new HashSet<>();
        List<HikeInfo> searchedHikes = new ArrayList<>();
        String query = "Select hike_id from hike_to_location where location_lat > ? and location_lat < ? and location_lng > ? and location_lng < ?";
        PreparedStatement pst = databaseConnector.getPreparedStatement(query);
        try {
            pst.setDouble(1, latFrom);
            pst.setDouble(2, latTo);
            pst.setDouble(3, lngFrom);
            pst.setDouble(4, lngTo);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(pst);
            while (resultSet.next()) {
                searchedHikeIds.add(resultSet.getInt(1));
            }
            for (Integer id : searchedHikeIds) {
                searchedHikes.add(hikeDM.getHikeById(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return searchedHikes;
    }

    /**
     * Gets list of hikes which match hike name parameter given in search.
     * @param searchedName name of hike which user searched
     * @return list of matching hikes
     */
    public List<HikeInfo> getSearchedHikesByHikeName(String searchedName) {
        List<HikeInfo> searchedHikes = new ArrayList<>();
        String query = "select ID from hikes where hike_name like ?";
        PreparedStatement pst = databaseConnector.getPreparedStatement(query);
        try {
            pst.setString(1, "%" + searchedName + "%");
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(pst);
            while (resultSet.next()) {
                searchedHikes.add(hikeDM.getHikeById(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchedHikes;
    }

    /**
     * Gets list of hikes which match member name parameter given in search.
     *
     * @return list of matching hikes
     */
    public List<HikeInfo> getSearchedHikesByMemberName(String ID) {
        String query = "select hike_id FROM hike_to_user inner join users where user_ID = users.id and users.id = ?";
        PreparedStatement pst = databaseConnector.getPreparedStatement(query);
        Set<Integer> searchedHikeIds = new HashSet<>();

        List<HikeInfo> searchedHikes = new ArrayList<>();
        try {
            pst.setString(1, ID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(pst);
            while (resultSet.next()) {
                searchedHikeIds.add(resultSet.getInt(1));
            }
            for (Integer id : searchedHikeIds) {
                searchedHikes.add(hikeDM.getHikeById(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchedHikes;
    }

    /**
     * Gets list of hikes which match time parameter given in search.
     * @param from starting date
     * @param to ending date
     * @return list of  matching hikes
     */
    public List<HikeInfo> getSearchedHikesByDate(String from, String to) {
        String query = "SELECT * from hikes where start_date > ? and end_date < ?";
        PreparedStatement pst = databaseConnector.getPreparedStatement(query);

        List<HikeInfo> searchedHikes = new ArrayList<>();
        try {
            pst.setString(1, from + "");
            pst.setString(2, to + "");
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(pst);
            while (resultSet.next()) {
                searchedHikes.add(hikeDM.getHikeById(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchedHikes;
    }


    /**
     * gets hikes from range min and max number of members in hike
     * @param min minimum number of going person
     * @param max maximum number of going person
     * @return
     */
    public List<HikeInfo> getSearchedHikesByMemberNumber(String min, String max){
        List<HikeInfo> searchedHikes = new ArrayList<>();

        String query = "SELECT * from hikes where max_people >= ? and max_people <= ?";
        PreparedStatement pst = databaseConnector.getPreparedStatement(query);

        try {
            pst.setString(1, min + "");
            pst.setString(2, max + "");
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(pst);
            while (resultSet.next()) {
                searchedHikes.add(hikeDM.getHikeById(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return searchedHikes;

    }
}






