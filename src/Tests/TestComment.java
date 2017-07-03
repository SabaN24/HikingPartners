package Tests;

import Models.Comment;
import Models.User;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Saba on 10.06.2017.
 */
public class TestComment {
    @Test
    public void TestComment() {
        User mockUser = new User(0, "bla", "bla", "bla");
        Date date = new Date();
        Comment comment = new Comment(0, "test1", 0, mockUser, date, 10);
        int ID = comment.getCommentID();
        assertEquals(0, ID);

        String commentText = comment.getComment();
        assertEquals("test1", commentText);

        int likeNum = comment.getLikeNUmber();
        assertEquals(10, likeNum);

        Date date1 = comment.getDate();
        assertEquals(date, date1);

        User mockUser1 = (User)comment.getUser();
        assertEquals(mockUser, mockUser1);

        User mockUser2 = new User(1, "bla", "bla", "bla");
        assertNotEquals(mockUser, mockUser2);
    }
}
