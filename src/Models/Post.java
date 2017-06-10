package Models;

import java.util.Date;
import java.util.ArrayList;

/**
 * Created by Saba on 10.06.2017.
 */
public class Post {

    private int id;
    private String text;
    private User user;
    private Date time;
    private int likes;
    private ArrayList<Comment> comments;

    public Post(int id, String text, User user, Date time){
        this.id = id;
        this.text = text;
        this.user = user;
        this.time = time;
        likes = 0;
        comments = new ArrayList<Comment>();
    }

    public int getID() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public Date getTime() {
        return time;
    }

    public int getLikes() {
        return likes;
    }

    public void addComment(Comment newComment){
        comments.add(newComment);
    }

    public int getCommentsNumber(){
        return comments.size();
    }

    public ArrayList<Comment> seeAllComments(){
        ArrayList<Comment> result = new ArrayList<Comment>();
        for(int i = 0; i < comments.size(); i++){
            result.add(comments.get(i));
        }
        return result;
    }

    public void incrementLikes(){
        likes++;
    }

    public void decrementLikes(){
        if(likes > 0){
            likes--;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Comment: " + id + " Text: " + text + " Time: " + time.toString() + " Likes: " + likes);
        return result.toString();
    }
}
