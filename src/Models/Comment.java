package Models;

import java.util.Date;

/**
 * Created by Saba on 10.06.2017.
 */
public class Comment {
    private int commentID;
    private int postID;
    private String comment;
    private User user;
    private Date date;
    private int likeNumber;
    private boolean isLiked;

    /**
     * Constructor
     * @param commentID
     * @param comment
     * @param user
     * @param date
     * @param likeNumber
     */
    public Comment(int commentID, String comment, int postID, User user, Date date, int likeNumber){
        this.commentID = commentID;
        this.comment = comment;
        this.user = user;
        this.date = date;
        this.likeNumber = likeNumber;
        this.postID = postID;
        this.isLiked = false;
    }

    public Comment(int commentID, String comment, int postID, User user, Date date, int likeNumber, boolean isLiked){
        this(commentID, comment, postID, user, date, likeNumber);
        this.isLiked = isLiked;
    }

    /**
     * Setter for isLiked
     * @param liked
     */
    public void setLiked(boolean liked) {
        isLiked = liked;
    }


    /**
     * @return Like number
     */
    public int getLikeNUmber(){
        return likeNumber;
    }

    /**
     * @return Comment ID
     */
    public int getCommentID(){
        return commentID;
    }

    /**
     * @return Comment text
     */
    public String getComment(){
        return comment;
    }

    /**
     * @return Commenter User
     */
    public User getUser(){
        return user;
    }

    /**
     * @return Comment Date
     */
    public Date getDate(){
        return date;
    }

    /**
     * @return Post ID
     */
    public int getPostID(){return postID;}
}
