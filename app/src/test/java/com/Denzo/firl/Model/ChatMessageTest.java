package com.Denzo.firl.Model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ChatMessageTest {

    @Test
    public void testChatMessageCreation() {
        long now = System.currentTimeMillis();
        ChatMessage msg = new ChatMessage("Hello", true, now);
        
        assertEquals("Hello", msg.getMessage());
        assertTrue(msg.isMine());
        assertEquals(now, msg.getTimestamp());
        assertEquals(ChatMessage.MessageType.TEXT, msg.getType());
    }

    @Test
    public void testChatMessageWithExtraInfo() {
        long now = System.currentTimeMillis();
        ChatMessage msg = new ChatMessage("Wanna meet?", false, now, ChatMessage.MessageType.DATE_INVITE, "Pizza at 8");
        
        assertEquals("Wanna meet?", msg.getMessage());
        assertFalse(msg.isMine());
        assertEquals(now, msg.getTimestamp());
        assertEquals(ChatMessage.MessageType.DATE_INVITE, msg.getType());
        assertEquals("Pizza at 8", msg.getExtraInfo());
    }
}
