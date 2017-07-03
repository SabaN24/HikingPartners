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

/**
 * Created by Nodo on 6/29/2017.
 */
public class ChatDM {


    private DatabaseConnector databaseConnector;

    public static final String ATTR = "ChatManager";

    private static ChatDM chm = null;

    private ChatDM() {
        databaseConnector = DatabaseConnector.getInstance();
    }

    public static ChatDM getInstance() {
        if (chm == null) {
            chm = new ChatDM();
        }
        return chm;
    }


    /**
     * gets chat messages bwtween these users
     * @param user1
     * @param user2
     * @return
     */
    public List<Message> getChatMessages(int user1, int user2){
        List<Message> messages = new ArrayList<>();

        User userFrom = MainDM.getInstance().getUserById(user1);

        User  userTo = MainDM.getInstance().getUserById(user2);


        String query = "SELECT * FROM Messages  WHERE from_user_id = " + "\"" + user1 + "\"" +  " and to_user_id  = " + "\"" +  user2 + "\";";

        ResultSet rs = databaseConnector.getData(query);

        try {
            while(rs.next()){

                int id = rs.getInt("ID");
                String messageText = rs.getString("message");
                Date date = rs.getDate("message_date");

                messages.add(new Message(id, userFrom, userTo, messageText, date));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userFrom = MainDM.getInstance().getUserById(user2);

        userTo = MainDM.getInstance().getUserById(user1);


        query = "SELECT * FROM Messages  WHERE from_user_id = " + "\"" + user2 + "\"" +  " and to_user_id  = " + "\"" +  user1 + "\";";

        rs = databaseConnector.getData(query);

        try {
            while(rs.next()){

                int id = rs.getInt("ID");
                String messageText = rs.getString("message");
                Date date = rs.getDate("message_date");

                messages.add(new Message(id, userFrom, userTo, messageText, date));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collections.sort(messages);

        return messages;
    }


    /**
     * addes new message with these parameters
     * @param fromUserId
     * @param toUserId
     * @param messageText
     * @param date
     * @return newly added message index
     */
    public int addMessage(int fromUserId, int toUserId, String messageText, Date date){
        String query = "insert into messages (from_user_id, to_user_id, message, message_date) values (?,?,?,?)";
        PreparedStatement newMessage = databaseConnector.getPreparedStatement(query);
        try {
            newMessage.setInt(1, fromUserId);
            newMessage.setInt(2, toUserId);
            newMessage.setString(3, messageText);
            newMessage.setDate(4,  new java.sql.Date(date.getTime()));
            databaseConnector.updateDataWithPreparedStatement(newMessage);

            ResultSet resultSet = databaseConnector.getData("select ID from messages order by ID desc limit 1");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }



    /**
     * returns signle chat between two user
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public Chat getChat(int fromUserId, int toUserId){

        User userTo = MainDM.getInstance().getUserById(toUserId);
        List<Message> messages = getChatMessages(fromUserId, toUserId);
        return new Chat(toUserId,userTo, messages);

    }


    /**
     * returns opened chats of given user
     * @param fromUserId
     * @return
     */
    public List<Chat> getChats(int fromUserId) {
        List<Chat> chats = new ArrayList<>();

        String query = "SELECT * FROM open_chats where from_user_id = "  +  "\"" +  fromUserId + "\";";

        ResultSet rs = databaseConnector.getData(query);

        try {
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
     * opens chat between these users
     * add in database
     * @param fromUserId
     * @param toUserId
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
     * method closes conversation between fromUser to toUSer
     * deletes from database appropriate value
     * @param fromUserId
     * @param toUserId
     */
    public void closeChat(int fromUserId, int toUserId){
        String query = "delete from open_chats  WHERE from_user_id = " + "\"" + fromUserId + "\"" +  " and to_user_id = " + "\"" +  toUserId + "\";";
        databaseConnector.updateData(query);
    }


}
