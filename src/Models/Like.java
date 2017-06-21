package Models;

/**
 * Created by Sandro on 21-Jun-17.
 */
public class Like {
    private int postID;
    private int commentID;

    public int getPostID() {
        return postID;
    }

    public int getCommentID() {
        return commentID;
    }

    public int getUserID() {
        return userID;
    }

    public boolean isLiked() {
        return liked;
    }

    private int userID;
    private boolean liked;

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
