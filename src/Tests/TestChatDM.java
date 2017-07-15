package Tests;

import Database.ChatDM;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Saba on 7/16/2017.
 * Tests for ChatDM class.
 */
public class TestChatDM {

    @Test
    public void testOpenChats(){
        int user1 = 1;
        int user2 = 2;
        ChatDM chatDM = ChatDM.getInstance();
        chatDM.openChat(1, 2);
        chatDM.addMessage(1, 2, "hi", new Date());
        assertEquals(1, chatDM.getChats(1).size());
        assertEquals(1, chatDM.getChatMessages(1, 2).size());
        chatDM.openChat(1, 3);
        assertEquals(2, chatDM.getChats(user1).size());
        chatDM.closeChat(user1, user2);
        assertEquals(1, chatDM.getChats(1).size());
    }

}
