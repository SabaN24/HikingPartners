package Models.Hike;

import Models.Photo;

import java.util.List;

/**
 * Created by Levani on 11.06.2017.
 */
public class DefaultModel {

    /**
     * Inner user class that is needed in hike page.
     */
    public static class User{
        /**
         * Private instance variables.
         */
        private int id;
        private String firstName;
        private String lastName;
        private String profilePictureAddress;

        /**
         * Constructor method.
         * @param id int
         * @param firstName String
         * @param lastName String
         * @param profilePictureAddress String
         */
        public User(int id, String firstName, String lastName, String profilePictureAddress) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.profilePictureAddress = profilePictureAddress;
        }

        /**
         * Returns database id of this user.
         * @return String
         */
        public int getId() {
            return id;
        }

        /**
         * Returns first name of user.
         * @return String
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * Returns last name of  user.
         * @return String
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * Returns profile picture address of  user.
         * @return String
         */
        public String getProfilePictureAddress() {
            return profilePictureAddress;
        }
    }

    /**
     * Private instance variables.
     */
    private DefaultModel.User creator;
    private List<Photo> coverPhotos;

    /**
     * Constructor method.
     * @param creator User
     * @param coverPhotos List<Photo>
     */
    public DefaultModel(User creator, List<Photo> coverPhotos) {
        this.creator = creator;
        this.coverPhotos = coverPhotos;
    }

    /**
     * Returns creator of hike.
     * @return User
     */
    public User getCreator() {
        return creator;
    }

    /**
     * Returns cover photos of hike.
     * @return List<Photo>
     */
    public List<Photo> getCoverPhotos() {
        return coverPhotos;
    }
}
