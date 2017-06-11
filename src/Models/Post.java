package Models;

import java.util.Date;
import java.util.ArrayList;

/**
 * Created by Saba on 10.06.2017.
 */
public class Post {

    //Private variables
    private int id;
    private String text;
    private User user;
    private Date time;
    private int likes;
    private ArrayList<Comment> comments;

    /**
     * Constructor of Post class
     *
     * @param id   id of post
     * @param text text of post
     * @param user author of post
     * @param time time in the moment when post was created
     */
    public Post(int id, String text, User user, Date time, ArrayList<Comment> comments) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.time = time;
        this.comments = comments;
        likes = 0;
    }

    /**
     * @return id of post
     */
    public int getID() {
        return id;
    }

    /**
     * @return text of post
     */
    public String getText() {
        return text;
    }

    /**
     * @return user who is the author of this post
     */
    public User getUser() {
        return user;
    }

    /**
     * @return time when comment was posted
     */
    public Date getTime() {
        return time;
    }

    /**
     * @return number of likes on this post
     */
    public int getLikes() {
        return likes;
    }

    /**
     * @return number of comments on this post
     */
    public int getCommentsNumber() {
        return comments.size();
    }

    /**
     * @return all comments on this post as an array list
     */
    public ArrayList<Comment> seeAllComments() {
        ArrayList<Comment> result = new ArrayList<Comment>();
        for (int i = 0; i < comments.size(); i++) {
            result.add(comments.get(i));
        }
        return result;
    }

    /**
     * Increments number of likes on post
     */
    public void incrementLikes() {
        likes++;
    }

    /**
     * Deccrements number of likes on post, if there are any
     */
    public void decrementLikes() {
        if (likes > 0) {
            likes--;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Comment: " + id + " Text: " + text + " Time: " + time.toString() + " Likes: " + likes);
        return result.toString();
    }

    @Override
    public boolean equals(Object o){
        Post other = (Post)o;
        return this.id == other.getID();
    }
}
