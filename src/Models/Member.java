package Models;

import java.util.Date;

/**
 * Created by Sandro on 01-Jul-17.
 * Keeps information about member in corresponding variables.
 */
public class Member extends User {
    private int roleID;

    /**
     * constructor of the user
     * gets these parameters:
     *
     * @param facebookID facebook id of user
     * @param firstName first name of user
     * @param lastName last name of user
     * @param profilePictureAddress url of profile picture of user
     * @param birthDate birth date of user
     * @param gender gender of user
     * @param email email of user
     * @param coverPictureAddress url of cover picture of user
     * @param facebookLink link to facebook of user
     */
    public Member(int id, String firstName, String lastName, String profilePictureAddress, long facebookID, Date birthDate, String gender, String email, String aboutMe, String coverPictureAddress, String facebookLink, int roleID) {
        super(id, firstName, lastName, profilePictureAddress, facebookID, birthDate, gender, email, aboutMe, coverPictureAddress, facebookLink);
        this.roleID = roleID;
    }

    /**
     * Returns role ID of memeber.
     */
    public int getRoleID(){
        return roleID;
    }
}
