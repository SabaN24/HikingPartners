package Models;

import java.util.Date;

/**
 * Created by Saba on 10.06.2017.
 */
public class User {

    /* Constants. */
    public static String ATTR = "User";

    /**
     * private instance variables
     */
    private int id;
    private String firstName;
    private String lastName;
    private String profilePictureAddress;
    private long facebookID;
    private Date birthDate;
    private String gender;
    private String email;
    private String aboutMe;
    private String coverPictureAddress;
    private String facebookLink;


    /**
     *constructor of the user
     * gets these parameters:
     * @param id
     * @param firstName
     * @param lastName
     * @param profilePictureAddress
     * @param facebookID
     * @param birthDate
     * @param gender
     * @param email
     * @param aboutMe
     * @param coverPictureAddress
     * @param facebookLink
     */
    public User(int id, String firstName, String lastName, String profilePictureAddress, long facebookID, Date birthDate, String gender, String email, String aboutMe, String coverPictureAddress , String facebookLink) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureAddress = profilePictureAddress;
        this.facebookID = facebookID;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.aboutMe = aboutMe;
        this.coverPictureAddress  = coverPictureAddress;
        this.facebookLink = facebookLink;
    }

    public void setBd(Date dt){
        this.birthDate = dt;
    }

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


    /**
     * returns facebook ID of this user
     * @return
     */
    public long getFacebookID() {
        return facebookID;
    }

    /**
     * returns birth date of this user
     * @return Date
     */
    public Date getBirthDate() {

        return birthDate;
    }


    /**
     * return gender of this user
     * @return  String
     */
    public String getGender() {

        return gender;
    }


    /**
     * return email of this user
     * @return String
     */
    public String getEmail() {

        return email;

    }

    /**
     * returns about me text of this user
     * @returns String
     */
    public String getAboutMe() {
        return aboutMe;
    }

    /**
     * returns facebook link of this user
     * @returns String
     */
    public String getFacebookLink(){
        return facebookLink;
    }

    /**
     * returns cover photo address of this user
     * @return
     */
    public String getCoverPictureAddress() {
        return coverPictureAddress;
    }
}
