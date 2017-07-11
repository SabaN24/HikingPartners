package Models.Hike;

import Models.Photo;
import Models.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Sandro on 11-Jul-17.
 */
public class HikeInfoExtended extends  HikeInfo{

    /* Constants */
    public static final int NOT_MEMBER = 0;
    public static final int MEMBER = 1;
    public static final int REQUEST_SENT = 2;

    /* Private instance variables */
    private int userStatus;

    /**
     * Constructor method.
     *
     * @param id
     * @param name
     * @param creator      User
     * @param coverPhotos  List<Photo>
     * @param maxPeople
     * @param joinedPeople
     * @param start
     * @param end
     * @param description
     */
    public HikeInfoExtended(int id, String name, User creator, List<Photo> coverPhotos, int maxPeople, int joinedPeople, Date start, Date end, String description, int userStatus) {
        super(id, name, creator, coverPhotos, maxPeople, joinedPeople, start, end, description);
        this.userStatus = userStatus;
    }
}
