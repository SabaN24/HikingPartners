package Database;

import Models.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by vache on 6/27/2017.
 */
public class LocationsDM {
    private DatabaseConnector databaseConnector;
    private static LocationsDM locationsDM = null;

    private LocationsDM(){databaseConnector = DatabaseConnector.getInstance();}

    public static LocationsDM getInstance() {
        if (locationsDM == null) {
            locationsDM = new LocationsDM();
        }
        return locationsDM;
    }

    /**
     * connect Hike and Location
     * @param hikeID
     * @param location_lat
     * @param location_lng
     * @param location_type_ID
     * @return newly added connection ID
     */
    public int connectHikeAndLocation(int hikeID, String location_lat, String location_lng, int location_type_ID){
        String query = "insert into hike_to_location (hike_ID, location_lat, location_lng, location_type_ID) values (?,?,?,?)";
        PreparedStatement connectHikeAndLoaction = databaseConnector.getPreparedStatement(query);
        try {
            connectHikeAndLoaction.setInt(1, hikeID);
            connectHikeAndLoaction.setString(2, location_lat);
            connectHikeAndLoaction.setString(3, location_lng);
            connectHikeAndLoaction.setInt(4, location_type_ID);
            databaseConnector.updateDataWithPreparedStatement(connectHikeAndLoaction);
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
     * Get list of locations by hike id;
     * @param hikeID
     * @return list of locations
     */
    public ArrayList<Location> getLocationsByHikeID(int hikeID){
        ArrayList<Location> locations = new ArrayList<>();
        String query = "select location_lat, location_lng, location_type_ID from hike_to_location where hike_id = ?";
        PreparedStatement locationsSt = databaseConnector.getPreparedStatement(query);
        try {
            locationsSt.setInt(1, hikeID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(locationsSt);
            while (resultSet.next()) {
                String locationLat = resultSet.getString(1);
                String locationLng = resultSet.getString(2);
                int locationTypeID = resultSet.getInt(3);
                Location location = new Location(hikeID, locationLat, locationLng, locationTypeID);
                locations.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

}
