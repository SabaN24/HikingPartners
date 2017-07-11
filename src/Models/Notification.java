package Models;

import java.util.Date;

/**
 * Created by Sandro on 11-Jul-17.
 */
public class Notification {
    /* Constants */
    public static final int REQUEST = 1;
    public static final int COMMENT = 2;
    public static final int LIKE = 3;

    /* Private instance variables. */
    private Integer ID;
    private Integer userID;
    private Date time;
    private Integer typeID;
    private Integer postID;
    private User fromUser;
    private Integer requestID;
    private Integer hikeID;
    private String hikeName;
    private Integer seen;

    public Notification(Integer ID, Integer userID, Date time, Integer typeID, Integer postID, User fromUser, Integer requestID, Integer hikeID, String hikeName, Integer seen){
        this.ID = ID;
        this.userID = userID;
        this.time = time;
        this.typeID = typeID;
        this.postID = postID;
        this.fromUser = fromUser;
        this.requestID = requestID;
        this.hikeID = hikeID;
        this.hikeName = hikeName;
        this.seen = seen;
    }
}
