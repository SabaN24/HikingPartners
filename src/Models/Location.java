package Models;

/**
 * Created by Saba on 10.06.2017.
 */
public class Location {
    private int hikeID;
    private String latitude;
    private String longitude;
    private int typeID;

    public Location(int hikeID, String latitude, String longitude, int typeID){
        this.hikeID = hikeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.typeID = typeID;
    }

    public int getHikeID(){
        return hikeID;
    }

    public String getLatitude(){
        return  latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public int getTypeID(){
        return typeID;
    }

    @Override
    public String toString(){
        return "Hike ID: " + hikeID + ", Location latitude: " + latitude + ", Location longitude: " + longitude + ", Location type ID: " + typeID;
    }
}
