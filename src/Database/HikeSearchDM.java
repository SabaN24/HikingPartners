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

    private HikeSearchDM() {
        databaseConnector = DatabaseConnector.getInstance();
        hikeDM = HikeDM.getInstance();
    }

    public static HikeSearchDM getInstance() {
        if (hikeSearchDM == null) {
            hikeSearchDM = new HikeSearchDM();
        }
        return hikeSearchDM;
    }

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

    public List<HikeInfo> getSearchedHikesByMemberName(String searchedName) {
        String query = "select hike_id FROM hike_to_user inner join users where user_ID = users.id and concat(concat(first_name, \" \"), last_name) like ?";
        PreparedStatement pst = databaseConnector.getPreparedStatement(query);
        Set<Integer> searchedHikeIds = new HashSet<>();

        List<HikeInfo> searchedHikes = new ArrayList<>();
        try {
            pst.setString(1, "%" + searchedName + "%");
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
}






