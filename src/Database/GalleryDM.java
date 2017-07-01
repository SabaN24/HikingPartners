package Database;

import Models.Photo;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Levani on 01.07.2017.
 */
public class GalleryDM {
    private static GalleryDM ourInstance = null;
    private final DatabaseConnector databaseConnector;

    public static GalleryDM getInstance() {
        if(ourInstance == null){
            ourInstance = new GalleryDM();
        }
        return ourInstance;
    }

    private GalleryDM() {
        databaseConnector = DatabaseConnector.getInstance();
    }


    public int savePhoto(int hikeID, int userID, String newPhoto){
        StringBuilder query = new StringBuilder("insert into gallery_photos (hike_ID, img_url, user_ID) values(");
        query.append("" + hikeID + ", ");
        query.append("\"" + newPhoto + "\", ");
        query.append("" + userID + ")");
        databaseConnector.updateData(query.toString());
        ResultSet resultSet = databaseConnector.getData("select ID from gallery_photos order by ID desc limit 1");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void deletePhoto(int photoID){
        String query = "delete from gallery_photos where ID = " + photoID;
        databaseConnector.updateData(query);
    }

    public Photo getGalleryPhoto(int photoID){
        String query = "select * from gallery_photos where ID = " + photoID;
        ResultSet resultSet = databaseConnector.getData(query);
        try {
            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String photoPath = resultSet.getString("img_url");
                String description = "";
                return new Photo(id, photoPath, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
