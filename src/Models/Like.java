package Models;

/**
 * Created by Sandro on 21-Jun-17.
 * Keeps information about like.
 */
public class Like {

    //Private variables
    private int postID;
    private int commentID;

    /**
     * @return id of post which is liked
     */
    public int getPostID() {
        return postID;
    }


    /**
     * @return id of post which is liked
     */
    public int getCommentID() {
        return commentID;
    }


    /**
     * @return id of post which is liked
     */
    public int getUserID() {
        return userID;
    }


    /**
     * @return id of post which is liked
     */
    public boolean isLiked() {
        return liked;
    }

    //Private variables
    private int userID;
    private boolean liked;

    //Constructors of like object depending on given parameters

    public Like(int commentID, int userID){
        this.postID = 0;
        this.commentID = commentID;
        this.userID = userID;
        this.liked = true;
    }

    public Like(int commentID, int userID, boolean liked){
        this(commentID, userID);
        this.postID = 0;
        this.liked = liked;
    }

    public Like(int postID, int commentID, int userID, boolean liked){
        this(commentID, userID, liked);
        this.postID = postID;
    }
}
