package Tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import Database.DatabaseConnector;

import Models.MiniUser;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.jupiter.api.BeforeEach;
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
        MiniUser miniUser = new MiniUser(ID, firstName, lastName, profilePictureAddress);

        assertEquals(ID, miniUser.getId());
        assertEquals(firstName, miniUser.getFirstName());
        assertEquals(lastName, miniUser.getLastName());
        assertEquals(profilePictureAddress, miniUser.getProfilePictureAddress());
    }
}
