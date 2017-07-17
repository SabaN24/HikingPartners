package Database;

import Models.Photo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Levani on 01.07.2017.
 * Data Manager which is responsible for adding photos to database and fetching them.
 */
public class GalleryDM {

    //Private variables
    private static GalleryDM ourInstance = null;
    private final DatabaseConnector databaseConnector;
    private ReentrantLock galleryLock;

    /**
     * getInstance method to make this class Signletone.
     * @return GalleryDm object
     */
    public static GalleryDM getInstance() {
        if(ourInstance == null){
            ourInstance = new GalleryDM();
        }
        return ourInstance;
    }

    /**
     * Private constructor which calls getInstance method.
     */
    private GalleryDM() {
        galleryLock = new ReentrantLock();
        databaseConnector = DatabaseConnector.getInstance();
    }


    /**
     * Adds given photo to gallery_photos table in database.
     * @param hikeID id of hike gallery of which receives a new photo
     * @param userID id of user who uploaded photo
     * @param newPhoto path of new photo
     * @return id of newly added photo
     */
    public int savePhoto(int hikeID, int userID, String newPhoto){
        StringBuilder query = new StringBuilder("insert into gallery_photos (hike_ID, img_url, user_ID) values (?,?,?);");
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query.toString());
        try {
            preparedStatement.setInt(1, hikeID);
            preparedStatement.setInt(3, userID);
            preparedStatement.setString(2, newPhoto);
            galleryLock.lock();
            databaseConnector.updateDataWithPreparedStatement(preparedStatement);
            int recentlyAdded = DatabaseHelper.getRecentlyAdded("gallery_photos");
            return  recentlyAdded;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            galleryLock.unlock();
        }
    }

    /**
     * Deletes photo from database
     * @param photoID id of photo which needs to be deleted
     */
    public void deletePhoto(int photoID){
        String query = "delete from gallery_photos where ID = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, photoID);
            databaseConnector.updateDataWithPreparedStatement(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets photo with given id.
     * @param photoID id of photo which needs to be fetched.
     * @return Photo object with given id
     */
    public Photo getGalleryPhoto(int photoID){
        String query = "select * from gallery_photos where ID = ?";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, photoID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(preparedStatement);
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

    /**
     * Gets all gallery photos for given hike.
     * @param hikeID id of hike.
     * @return All photos as a list.
     */
    public List<Photo> getGalleryPhotos(int hikeID){
        String query = "select * from gallery_photos where hike_ID = ?";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, hikeID);
            ResultSet resultSet = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            List<Photo> result = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String photoPath = resultSet.getString("img_url");
                String description = "";
                result.add(new Photo(id, photoPath, description));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
