package Models;

import Models.Hike.HikeInfo;

/**
 * Created by Saba on 7/4/2017.
 */
public class Request {

    private int id;
    private User sender;
    private User receiver;
    private HikeInfo hike;


    /**
     * Constructor of Request object
     *
     * @param id       id of request
     * @param sender   user who receives request
     * @param receiver user who sends request
     * @param hike
     */
    public Request(int id, User sender, User receiver, HikeInfo hike) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.hike = hike;
        this.id = id;
    }

    /**
     * @return id of request
     */
    public int getId() {
        return id;
    }

    /**
     * @return sender of request
     */
    public User getSender() {
        return sender;
    }


    /**
     * @return receiver of request
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * @return desired hike
     */
    public HikeInfo getHike() {
        return hike;
    }

    @Override
    public String toString() {
        return "Request number " + id + ": User " + sender.getId() +
                " sent request to " + receiver.getId() + " to join hike " + hike.getId();
    }

}
