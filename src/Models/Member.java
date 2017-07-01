package Models;

import java.util.Date;

/**
 * Created by Sandro on 01-Jul-17.
 */
public class Member extends User {
    private int roleID;

    /**
     * constructor of the user
     * gets these parameters:
     *
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
