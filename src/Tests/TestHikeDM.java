package Tests;

/**
 * Created by Saba on 7/16/2017.
 * Tests for HikeDM class.
 */

import Database.HikeDM;
import org.junit.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestHikeDM {
    @Test
    public void testHikeDM() {
        String name = "test";
        Date start = new Date();
        Date end = new Date();
        String description = "description";
        int maxPeople = 10;
        int creatorId = 5;
        HikeDM hikeDM = HikeDM.getInstance();
        int id = hikeDM.addNewHike(name, start, end, description, maxPeople, creatorId);
        assertEquals(name, hikeDM.getHikeById(id).getName());
        hikeDM.addUserToHike(id, 2, 2);
        assertEquals(creatorId, hikeDM.getHikeById(id).getCreator().getId());
        assertEquals(2, hikeDM.getHikeById(id).getJoinedPeople());
        hikeDM.addRequest(3, 1, id);
        hikeDM.addCoverPhoto("description", "path", id);
        assertEquals("path", hikeDM.getHikeById(id).getCoverPhotos().get(0).getSrc());
        assertEquals(1, hikeDM.getRequestedHikeIds(creatorId).size());
    }
}
