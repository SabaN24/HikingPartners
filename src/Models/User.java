package Models;

import java.util.Date;

/**
 * Created by Saba on 10.06.2017.
 */
public class User extends MiniUser{


    /**
     * private instance variables
     */
    private long facebookID;
    private Date birthDate;
    private String gender;
    private String email;


    /**
     * constructor of User class that extends Mini User
     * constructor calls super constructor for appropriate information for Mini Class
     * parameters needed super class
     * @param id
     * @param firstName
     * @param lastName
     * @param profilePictureAddress
     *
     * parameter needed for this class
     * @param facebookID
     * @param birthDate
     * @param gender
     * @param email
     */
    public User(int id, String firstName, String lastName, String profilePictureAddress, long facebookID, Date birthDate, String gender, String email) {
        super(id, firstName, lastName, profilePictureAddress);
        this.facebookID = facebookID;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
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


}
