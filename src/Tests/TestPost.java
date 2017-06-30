package Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.ArrayList;
import Models.Post;
import Models.Comment;
import Models.MiniUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Saba on 10.06.2017.
 */
public class TestPost {

    private Post p1;
    private Post p2;
    private MiniUser user1;
    private MiniUser user2;
    private Date date;
    private Comment c1;
    private Comment c2;
    private Comment c3;
    private Comment c4;
    private  ArrayList<Comment> comments;
    private ArrayList<Comment> empty;

    @BeforeEach
    public void testSetUp(){
        user1 = new MiniUser(1, "bla", "bla", "bla");
        user2 = new MiniUser(2, "bla", "bla", "bla");
        date = new Date();
        c1 = new Comment(1, "abc", 0, user1, date, 10);
        c2 = new Comment(2, "abcd", 0, user1, date, 10);
        c3 = new Comment(3, "abcde", 0, user2, date, 10);
        c4 = new Comment(4, "abcef", 0, user2, date, 10);
        comments = new ArrayList<Comment>();
        comments.add(c1);
        comments.add(c2);
        comments.add(c3);
        comments.add(c4);
        empty = new ArrayList<Comment>();
        p1 = new Post(1, "bla1", "bla", user1, date, comments, 10);
        p2 = new Post(2, "bla2", "bla", user2, date, empty, 9);
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
        assertEquals(p1.getLikes(), 10);
        assertEquals(p2.getLikes(), 9);
    }

    @Test
    public void testComments(){
        assertEquals(p1.getCommentsNumber(), 4);
        assertEquals(p2.getCommentsNumber(), 0);
        assertEquals(comments, p1.seeAllComments());
    }

}
