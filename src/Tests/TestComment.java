package Tests;

import Models.Comment;
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
        MockUser mockUser = new MockUser(0);
        Date date = new Date();
        Comment comment = new Comment(0, "test1", mockUser, date);
        comment.likeDecrease();
        comment.likeDecrease();
        comment.likeIncrease();
        comment.likeIncrease();
        comment.likeIncrease();
        comment.likeIncrease();
        comment.likeDecrease();
        int ID = comment.getCommentID();
        assertEquals(ID, 0);

        String commentText = comment.getComment();
        assertEquals(commentText, "test1");

        int likeNum = comment.getLikeNUmber();
        assertEquals(likeNum, 3);

        Date date1 = comment.getDate();
        assertEquals(date1, date);

        MockUser mockUser1 = (MockUser)comment.getUser();
        assertEquals(mockUser1, mockUser);

        MockUser mockUser2 = new MockUser(1);
        assertNotEquals(mockUser2, mockUser);
    }
}
