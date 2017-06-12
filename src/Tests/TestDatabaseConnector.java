package Tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import Database.DatabaseConnector;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by vache on 6/12/2017.
 */
public class TestDatabaseConnector {

    private DatabaseConnector db;
    private String insertQuery1;
    private String insertQuery2;
    private String selectQuery1;
    private String updateQuery1;

    @BeforeEach
    public void testSetup(){
        insertQuery1 = "insert into users values (11, 'Vache', 'Katsadze')";
        insertQuery2 = "insert into users values (12, 'Vache', 'Katsadze')";
        selectQuery1 = "select * from users";
        updateQuery1 = "update users set first_name = \"Vache1\", last_name = \"Katsadze1\" where ID = 11";
        db = DatabaseConnector.getInstance();
    }

    @Test
    public void testExceptions() {
        Boolean b1 = false;
        Boolean b2 = false;
        Boolean b3 = false;
        db.getData("Bad Statement");
        db.updateData("Bad Statement");
        db.getData(selectQuery1);

        assertEquals(true, b1);
        assertEquals(true, b2);
        assertNotEquals(true, b3);
    }

    @Test
    public void testGetUpdate1() throws SQLException{
        db.updateData(insertQuery1);
        ResultSet resultSet = db.getData(selectQuery1);

        int userID = -1;
        String userName = null;
        String userSurname = null;

        while(resultSet.next()) {
            userID = resultSet.getInt(1);
            userName = resultSet.getString(2);
            userSurname = resultSet.getString(3);
        }

        assertEquals(11, userID);
        assertEquals("Vache", userName);
        assertEquals("Katsadze", userSurname);

        db.updateData("delete from users where ID = 11");
    }

    @Test
    public void testGetUpdate2() throws SQLException{
        db.updateData("insert into users values (100, 'A', 'B')");
        db.updateData(insertQuery2);

        db.updateData("delete from users where ID = 12");

        ResultSet resultSet = db.getData(selectQuery1);

        int userID = -1;
        String userName = null;
        String userSurname = null;

        while(resultSet.next()) {
            userID = resultSet.getInt(1);
            userName = resultSet.getString(2);
            userSurname = resultSet.getString(3);
        }

        assertEquals(100, userID);
        assertEquals("A", userName);
        assertEquals("B", userSurname);

        db.updateData("delete from users where ID = 12");
        db.updateData("delete from users where ID = 100");
    }

    @Test
    public void testGetUpdate3() throws SQLException{
        db.updateData(insertQuery1);
        db.updateData(updateQuery1);

        ResultSet resultSet = db.getData(selectQuery1);

        int userID = -1;
        String userName = null;
        String userSurname = null;

        while(resultSet.next()) {
            userID = resultSet.getInt(1);
            userName = resultSet.getString(2);
            userSurname = resultSet.getString(3);
        }

        assertEquals(11, userID);
        assertEquals("Vache1", userName);
        assertEquals("Katsadze1", userSurname);

        db.updateData("delete from users where ID = 11");
    }
}
