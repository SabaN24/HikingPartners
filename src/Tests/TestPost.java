package Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.ArrayList;
import Models.Post;
import Models.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Saba on 10.06.2017.
 */
public class TestPost {

    private Post p1;
    private Post p2;
    private MockUser user1;
    private MockUser user2;
    private Date date;

    @BeforeEach
    public void testSetUp(){
        user1 = new MockUser(1);
        user2 = new MockUser(2);
        date = new Date();
        p1 = new Post(1, "bla1", user1, date);
        p2 = new Post(2, "bla2", user2, date);
    }

    @Test
    public void testGetID(){
        assertEquals(p1.getID(), 1);
        assertEquals(p2.getID(), 2);
    }

    @Test
    public void testGetText(){
        assertEquals(p1.getText(), "bla1");
        assertEquals(p2.getText(), "bla2");
    }

    @Test
    public void testGetUser(){
        assertEquals(p1.getUser(), user1);
        assertNotEquals(p2.getUser(), user1);
        assertEquals(p2.getUser(), user2);
        assertNotEquals(p1.getUser(), user2);
    }

    @Test
    public void testGetTime(){
        assertEquals(p1.getTime(), date);
    }

    @Test
    public void testLikes(){
        p1.incrementLikes();
        p1.incrementLikes();
        p1.incrementLikes();
        assertEquals(p1.getLikes(), 3);
        p1.decrementLikes();
        assertEquals(p1.getLikes(), 2);
        p2.decrementLikes();
        assertEquals(p2.getLikes(), 0);
    }

    @Test
    public void testComments(){
        Comment c1 = new Comment(1, "abc", user1, date);
        Comment c2 = new Comment(2, "abcd", user1, date);
        Comment c3 = new Comment(3, "abcde", user2, date);
        Comment c4 = new Comment(4, "abcef", user2, date);
        p1.addComment(c1);
        p1.addComment(c2);
        assertEquals(p1.getCommentsNumber(), 2);
        assertEquals(p2.getCommentsNumber(), 0);
        p2.addComment(c3);
        p2.addComment(c4);
        ArrayList<Comment> comments = new ArrayList<Comment>();
        comments.add(c3);
        comments.add(c4);
        assertEquals(comments, p2.seeAllComments());
    }

}
