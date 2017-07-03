package Models;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

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
    private String link;

    public Photo getPhoto() {
        return photo;
    }

    private Photo photo;
    private List<Comment> comments;

    /**
     * Constructor of Post class
     *
     * @param id    id of post
     * @param text  text of post
     * @param user  author of post
     * @param time  time in the moment when post was created
     * @param likes like number
     */
    public Post(int id, String text, String link, User user, Date time, List<Comment> comments, int likes, Photo photo) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.time = time;
        this.link = formatLink(link);
        this.comments = comments;
        this.likes = likes;
        this.photo = photo;
    }

    /**
     * Converts regular youtube link into embed link which will be inserted in HTML.
     * @param link regular youtube watch link
     * @return embed link
     */
    private static String formatLink(String link) {
        StringBuilder result = new StringBuilder("");
        if(link == null || link.length() < "youtube.com/watch?v=".length()){
            return result.toString();
        }
        int i = 0;
        while (!link.substring(i, i + "watch?v=".length()).equals("watch?v=")) {
            result.append(link.charAt(i));
            i++;
        }
        i += "watch?v=".length();
        result.append("embed/");
        result.append(link.substring(i));
        return result.toString();
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
     * @return youtube link
     */
    public String getLink() {
        return link;
    }

    /**
     * @return all comments on this post as an array list
     */
    public List<Comment> seeAllComments() {
        List<Comment> result = new ArrayList<Comment>();
        for (int i = 0; i < comments.size(); i++) {
            result.add(comments.get(i));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Comment: " + id + " Text: " + text + " Time: " + time.toString() + " Likes: " + likes);
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        Post other = (Post) o;
        return this.id == other.getID();
    }

}
