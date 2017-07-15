package Models;

import java.util.Date;

/**
 * Created by Nodo on 6/28/2017.
 * Keeps infromation about single message.
 */
public class Message implements Comparable {


    //Private variables
    private int id;
    private User userFrom;
    private User userTo;
    private String message;
    private Date date;


    /**
     * Constructor of message object
     * @param id id of message
     * @param userFrom id of sender of message
     * @param userTo id of receiver of message
     * @param message text of message
     * @param date date when message was sent
     */
    public Message(int id, User userFrom, User userTo, String message, Date date) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.message = message;
        this.date = date;
    }

    /**
     * @return id of message
     */
    public int getId() {
        return id;
    }

    /**
     * @return id of sender of message
     */
    public User getUserFrom() {
        return userFrom;
    }

    /**
     * @return id of receiver of message
     */
    public User getUserTo() {
        return userTo;
    }

    /**
     * @return text of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return date of message
     */
    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Object obj) {
        Message other = (Message) obj;
        return this.date.compareTo(other.date);
    }
}
