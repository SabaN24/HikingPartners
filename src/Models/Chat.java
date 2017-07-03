package Models;

import java.util.List;

/**
 * Created by Nodo on 7/2/2017.
 */
public class Chat {


    private int toUserId;
    private User userTo;
    private List<Message> messages;


    public Chat(int toUserId, User userTo, List<Message> messages) {
        this.toUserId = toUserId;
        this.userTo = userTo;
        this.messages = messages;
    }

    public int getToUserId() {
        return toUserId;
    }

    public User getUserTo() {
        return userTo;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
