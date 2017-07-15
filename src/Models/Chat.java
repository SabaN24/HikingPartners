package Models;

import java.util.List;

/**
 * Created by Nodo on 7/2/2017.
 * Keeps info about chat in corresponding variables.
 */
public class Chat {


    //Private variables
    private int toUserId;
    private User userTo;
    private List<Message> messages;


    /**
     * Constructor of chat object.
     *
     * @param toUserId chat participant 1 id
     * @param userTo   chat participant 2 id
     * @param messages list of messages in chat
     */
    public Chat(int toUserId, User userTo, List<Message> messages) {
        this.toUserId = toUserId;
        this.userTo = userTo;
        this.messages = messages;
    }

    /**
     * @return id of chat participant 1
     */
    public int getToUserId() {
        return toUserId;
    }


    /**
     * @return id of chat participant 2
     */
    public User getUserTo() {
        return userTo;
    }

    /**
     * @return list of messages in chat
     */
    public List<Message> getMessages() {
        return messages;
    }
}
