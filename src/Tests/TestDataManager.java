package Tests;

import Models.Hike.AboutModel;
import Database.DataManager;
import Models.Hike.DefaultModel;
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
        dataManager = DataManager.getInstance();
    }

    /**
     * test getAboutModel()
     * @throws SQLException
     */
    @Test
    public void testGetAboutModel()throws SQLException{

        DefaultModel defaultModel = dataManager.getDefaultModel(1);
        AboutModel aboutModel = dataManager.getAboutModel(1, 1);

        assertEquals("MAGARI PAXODI!!! SHEMODIT ALL :***", defaultModel.getName());
        assertEquals(5, aboutModel.getMaxPeople());

        assertEquals(5, aboutModel.getComments().size());
        assertEquals("nashoba iqneba???", aboutModel.getComments().get(4).getComment());

    }


}
