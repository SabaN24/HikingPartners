package Tests;

import Models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMiniUser {
    @Test
    public void testMiniUser(){
        int ID = 0;
        String firstName = "Vache";
        String lastName = "Katsadze";
        String profilePictureAddress = "address";
        User miniUser = new User(ID, firstName, lastName, profilePictureAddress);

        assertEquals(ID, miniUser.getId());
        assertEquals(firstName, miniUser.getFirstName());
        assertEquals(lastName, miniUser.getLastName());
        assertEquals(profilePictureAddress, miniUser.getProfilePictureAddress());
    }
}
