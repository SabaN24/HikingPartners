package Tests;

import Models.Hike.*;
import Models.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Levani on 11.06.2017.
 */
public class TestHikeDefaultModel {

    private DefaultModel defaultModel;
    private DefaultModel.User user;
    private List<Photo> photos;

    @BeforeEach
    public void testSetup(){
        //user setup
        int id = 5;
        String firstName = "Levan";
        String lastName = "Beroshvili";
        String profilePictureAddress = "pic.jpg";
        user = new DefaultModel.User(id, firstName, lastName, profilePictureAddress);

        //photos setup
        Photo photo1 = new Photo(1, "test", "Batumi", "aa");
        Photo photo2 = new Photo(0, "test1", "Batumi", "aa");
        photos = new ArrayList<>();
        photos.add(photo1);
        photos.add(photo2);

        //default model setup
        defaultModel = new DefaultModel(user, photos);
    }

    /**
     * Tests getters for DefaultModel.User
     */
    @Test
    public void testUser(){
        assertEquals(user.getFirstName(), "Levan");
        assertEquals(user.getLastName(), "Beroshvili");
        assertEquals(user.getId(), 5);
        assertEquals(user.getProfilePictureAddress(), "pic.jpg");
    }

    /**
     * Tests getters for DefaultModel
     */
    @Test
    public void testDefaultModel(){
        assertEquals(defaultModel.getCreator(), user);
        assertEquals(defaultModel.getCoverPhotos(), photos);
    }

}
