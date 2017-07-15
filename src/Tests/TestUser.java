package Tests;

import Models.User;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Saba on 10.06.2017.
 */
public class TestUser {
    @Test
    public void testUser(){
        User user = new User(1, "first", "last", "profilePic", 1234, new Date(), "dog", "email", "hello", "coverPic", "link");
        assertEquals(1, user.getId());
        assertEquals("first", user.getFirstName());
        assertEquals("last", user.getLastName());
        assertEquals("profilePic", user.getProfilePictureAddress());
        assertEquals(1234, user.getFacebookID());
        assertEquals("dog", user.getGender());
    }
}