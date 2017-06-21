package Models;

/**
 * Created by Sandro on 21-Jun-17.
 */
public class Like {
    private int commentID;
    private int userID;
    private boolean liked;

    public Like(int commentID, int userID, boolean liked){
        this.commentID = commentID;
        this.userID = userID;
        this.liked = liked;
    }
}
