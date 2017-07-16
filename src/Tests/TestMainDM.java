package Tests;

import Database.MainDM;
import Models.Hike.AboutModel;
import Models.Hike.DefaultModel;
import org.junit.Assert;
import org.junit.Test;
import sun.applet.Main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Saba on 7/16/2017.
 * Tests for MainDM class.
 */

public class TestMainDM {

    @Test
    public void testMainDM() {
        MainDM main = MainDM.getInstance();
        assertEquals("Saba", main.getUserById(3).getFirstName());
        assertEquals("Natroshvili", main.getUserById(3).getLastName());
        assertNotEquals(null, main.getUserById(3).getCoverPictureAddress());
        DefaultModel defaultModel = main.getDefaultModel(1);
        assertEquals(1, defaultModel.getCreator().getId());
        assertEquals("Jikia", defaultModel.getCreator().getLastName());
        assertEquals("მოლაშქრეთა კლუბი აიეტი", defaultModel.getName());
        AboutModel aboutModel = main.getAboutModel(1, 1);
        assertEquals(5, aboutModel.getComments().size());
        assertEquals(5, aboutModel.getMaxPeople());
        assertEquals("ტურის ორგანიზატორია მოლაშქრეთა კლუბი აიეტი, გასვლის და დაბრუნების თარიღი : 15 ივლისი " +
                "- 16 ივლისი, მთავარი ლოკაციები : თბილისი, ბათმი, თურქეთი, ყაზბეგი", aboutModel.getDescription());
        assertEquals(3, main.getPosts(1, 1).size());
        assertEquals(4, main.getHikeMembers(1).size());
        assertEquals(3, main.getNotifications(3).size());
        assertEquals(1, defaultModel.getCoverPhotos().size());
        assertEquals(1, main.getHikes(1).size());
    }

}
