package Models;

/**
 * Created by Saba on 10.06.2017.
 * Keeps information about location.
 */
public class Location {

    //Private variables
    private int hikeID;
    private String latitude;
    private String longitude;
    private int typeID;

    /**
     * Creates location object with given parameters
     * @param hikeID id of location
     * @param latitude latitude of location
     * @param longitude longitude of location
     * @param typeID id of type of locations
     */
    public Location(int hikeID, String latitude, String longitude, int typeID){
        this.hikeID = hikeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.typeID = typeID;
    }

    public int getHikeID(){
        return hikeID;
    }

    /**
     * @return latitude of location
     */
    public String getLatitude(){
        return  latitude;
    }

    /**
     * @return latitude of location
     */
    public String getLongitude(){
        return longitude;
    }

    /**
     * @return id of type of location
     */
    public int getTypeID(){
        return typeID;
    }

    @Override
    public String toString(){
        return "Hike ID: " + hikeID + ", Location latitude: " + latitude + ", Location longitude: " + longitude + ", Location type ID: " + typeID;
    }
}
