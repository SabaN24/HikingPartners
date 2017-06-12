package Tests;

import Models.Hike.AboutModel;
import Database.DataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by Nodo on 6/12/2017.
 */
public class TestDataManager {

    private DataManager dataManager;

    @BeforeEach
    public void testSetup(){
        dataManager = new DataManager();
    }

    /**
     * test getAboutModel()
     * @throws SQLException
     */
    @Test
    public void testGetAboutModel()throws SQLException{

        AboutModel aboutModel = dataManager.getAboutModel(1);

        assertEquals(1,aboutModel.getID());
        assertEquals("KVELAZE MAGARI PAXODE", aboutModel.getName());
        assertEquals("MAGARI PAXODI!!! SHEMODIT ALL :***", aboutModel.getDescription());
        assertEquals(5, aboutModel.getMaxPeople());

        assertEquals(5, aboutModel.getComments().size());
        assertEquals("nashoba iqneba???", aboutModel.getComments().get(4).getComment());

    }


}
