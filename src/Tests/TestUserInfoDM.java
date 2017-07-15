package Tests;

import Database.UserInfoDM;
import org.junit.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Saba on 7/16/2017.
 * Tests for UserInfoDM
 */

public class TestUserInfoDM {

    @Test
    public void testUserInfoDM() {
        UserInfoDM u = UserInfoDM.getInstance();
        int id = u.registerUser(1234, "first", "last", "profileUrl", new java.sql.Date(System.currentTimeMillis()), "male", "email", "coverUrl", "fbLink");
        assertEquals(true, u.isUserRegistered(1234));
        assertEquals(false, u.isUserRegistered(3213));
        assertEquals("male", u.getUserByFacebookkID(1234).getGender());
        assertEquals(1234, u.getUserByID(id).getFacebookID());
        u.updateAboutMeInfo(id, "new text");
        assertEquals("new text", u.getUserByID(id).getAboutMe());
        assertEquals("first", u.getUserByFacebookkID(1234).getFirstName());
    }

}
