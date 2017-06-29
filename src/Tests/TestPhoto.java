package Tests;

import Models.Photo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by vache on 6/11/2017.
 */
public class TestPhoto {

    @Test
    public void TestPhoto() {
        Photo photo = new Photo(0, "test",  "batumiaa");
        assertEquals("test", photo.getSrc());
        assertEquals(0, photo.getID());
        assertEquals("batumiaa", photo.getDescription());

        Photo photo1 = new Photo(1, "test",  "aa");
        Photo photo2 = new Photo(0, "test1",  "aa");
        assertNotEquals(photo1, photo);
        assertEquals(photo2, photo);
    }

}
