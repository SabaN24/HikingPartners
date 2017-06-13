package Models;

import java.util.Date;

/**
 * Created by Saba on 10.06.2017.
 */
public class Comment {
    private int commentID;
    private String comment;
    private MiniUser user;
    private Date date;
    private int likeNumber;

    /**
     * Constructor
     * @param commentID
     * @param comment
     * @param user
     * @param date
     * @param likeNumber
     */
    public Comment(int commentID, String comment, MiniUser user, Date date, int likeNumber){
        this.commentID = commentID;
        this.comment = comment;
        this.user = user;
        this.date = date;
        this.likeNumber = likeNumber;
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
    public MiniUser getUser(){
        return user;
    }

    /**
     * @return Comment Date
     */
    public Date getDate(){
        return date;
    }
}
