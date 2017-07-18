package Database;

import Models.Chat;
import Models.Message;
import Models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Nodo on 6/29/2017.
 * DatabaseManager class which is responsible for fetching messages from database and adding them to database.
 */
public class ChatDM {


    //Private variables

    private DatabaseConnector databaseConnector;

    public static final String ATTR = "ChatManager";

    private static ChatDM chm = null;

    private ReentrantLock lockMessages;

    /**
     * Private constructor of ChatDM object (Singletone pattern)
     */
    private ChatDM() {
        databaseConnector = DatabaseConnector.getInstance();
        lockMessages = new ReentrantLock();
    }


    /**
     * getInstance method so that class is singletone.
     * @return ChatDM object
     */
    public static ChatDM getInstance() {
        if (chm == null) {
            chm = new ChatDM();
        }
        return chm;
    }


    /**
     * Gets chat messages between users with given ids.
     * @param user1 id of first user
     * @param user2 of second user
     * @return list of messages
     */
    public List<Message> getChatMessages(int user1, int user2){
        List<Message> messages = new ArrayList<>();
        User userFrom = MainDM.getInstance().getUserById(user1);
        User  userTo = MainDM.getInstance().getUserById(user2);
        String query = "SELECT * FROM Messages  WHERE from_user_id = ? and to_user_id  = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, user1);
            preparedStatement.setInt(2, user2);
            ResultSet rs = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            while(rs.next()){
                int id = rs.getInt("ID");
                String messageText = rs.getString("message");
                Date date = (Date)rs.getObject("message_date");
                messages.add(new Message(id, userFrom, userTo, messageText, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userFrom = MainDM.getInstance().getUserById(user2);
        userTo = MainDM.getInstance().getUserById(user1);
        query = "SELECT * FROM Messages  WHERE from_user_id = ? and to_user_id = ?;";
        PreparedStatement preparedStatement1 = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement1.setInt(1, user2);
            preparedStatement1.setInt(2, user1);
            ResultSet rs = databaseConnector.getDataWithPreparedStatement(preparedStatement1);
            while(rs.next()){
                int id = rs.getInt("ID");
                String messageText = rs.getString("message");
                Date date = (Date)rs.getObject("message_date");
                messages.add(new Message(id, userFrom, userTo, messageText, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.sort(messages);
        return messages;
    }


    /**
     * Adds new message to database with given parameters.
     * @param fromUserId id of user who sent message
     * @param toUserId id of user who receives message
     * @param messageText text of message
     * @param date date when the message was sent
     * @return newly added message index
     */
    public int addMessage(int fromUserId, int toUserId, String messageText, String date){
        String query = "insert into messages (from_user_id, to_user_id, message, message_date) values (?,?,?,?)";
        PreparedStatement newMessage = databaseConnector.getPreparedStatement(query);
        try {
            newMessage.setInt(1, fromUserId);
            newMessage.setInt(2, toUserId);
            newMessage.setString(3, messageText);
            newMessage.setString(4, date);
            lockMessages.lock();
            databaseConnector.updateDataWithPreparedStatement(newMessage);
            int recentlyAdded = DatabaseHelper.getRecentlyAdded("messages");
            return recentlyAdded;
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
        finally {
            lockMessages.unlock();
        }
    }



    /**
     * Returns chat object for given users
     * @param fromUserId chat participant 1
     * @param toUserId chat participant 2
     * @return Chat object
     */
    public Chat getChat(int fromUserId, int toUserId){
        User userTo = MainDM.getInstance().getUserById(toUserId);
        List<Message> messages = getChatMessages(fromUserId, toUserId);
        return new Chat(toUserId,userTo, messages);
    }


    /**
     * Returns chats which are currently opened by given user.
     * @param fromUserId id of user
     * @return list of opened chats
     */
    public List<Chat> getChats(int fromUserId) {
        List<Chat> chats = new ArrayList<>();
        String query = "SELECT * FROM open_chats where from_user_id = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, fromUserId);
            ResultSet rs = databaseConnector.getDataWithPreparedStatement(preparedStatement);
            while(rs.next()){
                int toUserId  = rs.getInt("to_user_id");
                chats.add(getChat(fromUserId, toUserId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chats;
    }


    /**
     * Opens chat between users with given ids.
     * Adds newly opened chat to database.
     * @param fromUserId id of chat participant 1
     * @param toUserId id of chat participant 2
     */
    public void openChat(int fromUserId, int toUserId){
        String query = "insert into open_chats (from_user_id, to_user_id) values (?,?)";
        PreparedStatement st = databaseConnector.getPreparedStatement(query);
        try {
            st.setInt(1, fromUserId);
            st.setInt(2, toUserId);
            databaseConnector.updateDataWithPreparedStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Closes chat between users with given ids.
     * Deletes chat from database table.
     * @param fromUserId
     * @param toUserId
     */
    public void closeChat(int fromUserId, int toUserId){
        String query = "delete from open_chats  WHERE from_user_id = ? and to_user_id = ?;";
        PreparedStatement preparedStatement = databaseConnector.getPreparedStatement(query);
        try {
            preparedStatement.setInt(1, fromUserId);
            preparedStatement.setInt(2, toUserId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseConnector.updateDataWithPreparedStatement(preparedStatement);
    }


}
