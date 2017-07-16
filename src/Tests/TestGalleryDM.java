package Tests;

import Database.GalleryDM;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Saba on 7/16/2017.
 * Tests for GalleryDM class.
 */
public class TestGalleryDM {
    @Test
    public void testGalleryDM(){
        int photoId = 1;
        String url = "bla";
        GalleryDM g = GalleryDM.getInstance();
        g.savePhoto(1, 1, url);
        g.savePhoto(1, 2, "bla1");
        assertEquals(2, g.getGalleryPhotos(1).size());
        g.deletePhoto(photoId);
        assertEquals(1, g.getGalleryPhotos(1).size());
    }
}
