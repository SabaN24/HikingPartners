package Models;

import java.util.Date;

/**
 * Created by Nodo on 6/28/2017.
 */
public class Message implements Comparable {


    private int id;
    private User userFrom;
    private User userTo;
    private String message;
    private Date date;


    public Message(int id, User userFrom, User userTo, String message, Date date) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.message = message;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Object obj) {
        Message other = (Message) obj;
        return this.date.compareTo(other.date);
    }
}
