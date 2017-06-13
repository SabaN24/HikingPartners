package Models;

/**
 * MiniUser class to represent minimal information about user.
 * Created by Saba on 13.06.2017.
 */

public class MiniUser{
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
    public MiniUser(int id, String firstName, String lastName, String profilePictureAddress) {
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

    @Override
    public String toString(){
        return firstName + " " + lastName + " " + "Image url: " + profilePictureAddress;
    }
}
