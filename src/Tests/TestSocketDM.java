package Tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import Database.DatabaseConnector;
import Database.HikeFeedSocketDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import Models.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by vache on 6/15/2017.
 */
public class TestSocketDM {
    private HikeFeedSocketDM socketDM;
    private DatabaseConnector dbConnector;

    @BeforeEach
    public void testSetup(){
        socketDM = new HikeFeedSocketDM();
        dbConnector = DatabaseConnector.getInstance();
    }

    @Test
    public void testWritePost(){
        int userID = 1;
        int hikeID = 1;
        String post = "TestTestTest";
        int returnedID = socketDM.writePost(userID, hikeID, post);
        assertNotEquals(-1, returnedID);

        ResultSet resultSet = dbConnector.getData("select * from posts order by ID desc limit 1");
        try {
            if(resultSet.next()){
                assertEquals("TestTestTest", resultSet.getString(2));
                assertEquals(hikeID, resultSet.getInt(3));
                assertEquals(userID, resultSet.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnector.updateData("delete from posts where ID = " + returnedID);
    }

    @Test
    public void testAddComment(){
        int userID = 1;
        int postID = 1;
        int hikeID = 1;
        String comment = "TestTestTest";
        int privacyType = 1;
        int returnedID = socketDM.addComment(userID, postID, hikeID, comment, privacyType);
        assertNotEquals(-1, returnedID);

        ResultSet resultSet = dbConnector.getData("select * from comments order by ID desc limit 1");
        try {
           if(resultSet.next()){
               assertEquals(comment, resultSet.getString(2));
               assertEquals(0, resultSet.getInt(3));
               assertEquals(hikeID, resultSet.getInt(4));
               assertEquals(userID, resultSet.getInt(5));
               assertEquals(privacyType, resultSet.getInt(7));
           }
        } catch (SQLException e) {
           e.printStackTrace();
        }

        dbConnector.updateData("delete from comments where ID = " + returnedID);

        //-------------------------------------------------------------------

        int returnedID2 = socketDM.writePost(1, 1, "Test");
        int returnedID1 = socketDM.addComment(userID, returnedID2, hikeID, comment+"1", privacyType + 1);
        assertNotEquals(-1, returnedID1);

        ResultSet resultSet1 = dbConnector.getData("select * from comments order by ID desc limit 1");
        try {
            if(resultSet1.next()){
                assertEquals(comment+"1", resultSet1.getString(2));
                assertEquals(returnedID2, resultSet1.getInt(3));
                assertEquals(hikeID, resultSet1.getInt(4));
                assertEquals(userID, resultSet1.getInt(5));
                assertEquals(privacyType + 1, resultSet1.getInt(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnector.updateData("delete from comments where ID = " + returnedID1);
        dbConnector.updateData("delete from posts where ID = " + returnedID2);
    }

    @Test
    public void testLikePost(){
        int returnedID = socketDM.writePost(1, 1, "Test");
        int userID = 1;
        int returnedID1 = socketDM.likePost(userID, returnedID);
        assertNotEquals(-1, returnedID1);

        ResultSet resultSet = dbConnector.getData("select * from post_likes order by ID desc limit 1");
        try {
            if(resultSet.next()){
                assertEquals(returnedID, resultSet.getInt(2));
                assertEquals(userID, resultSet.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnector.updateData("delete from post_likes where ID = " + returnedID1);
        dbConnector.updateData("delete from posts where ID = " + returnedID);
    }

    @Test
    public void testLikeComment() {
        int returnedID = socketDM.writePost(1, 1, "Test");
        int returnedID1 = socketDM.addComment(1, 1, 1, "test", 1);
        int userID = 1;
        int returnedID2 = socketDM.likeComment(userID, returnedID1);
        assertNotEquals(-1, returnedID2);

        ResultSet resultSet = dbConnector.getData("select * from comment_likes order by ID desc limit 1");
        try {
            if (resultSet.next()) {
                assertEquals(userID, resultSet.getInt(3));
                assertEquals(returnedID1, resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnector.updateData("delete from comment_likes where ID = " + returnedID2);
        dbConnector.updateData("delete from comments where ID = " + returnedID1);
        dbConnector.updateData("delete from posts where ID = " + returnedID);
    }
}
