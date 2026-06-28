package com.Denzo.firl.Model;

public class ChatMessage {
    public enum MessageType {
        TEXT, DATE_INVITE, ICEBREAKER
    }

    private String message;
    private boolean isMine;
    private long timestamp;
    private MessageType type = MessageType.TEXT;
    private String extraInfo;

    public ChatMessage(String message, boolean isMine, long timestamp) {
        this.message = message;
        this.isMine = isMine;
        this.timestamp = timestamp;
    }

    public ChatMessage(String message, boolean isMine, long timestamp, MessageType type, String extraInfo) {
        this.message = message;
        this.isMine = isMine;
        this.timestamp = timestamp;
        this.type = type;
        this.extraInfo = extraInfo;
    }

    public String getMessage() {
        return message;
    }

    public boolean isMine() {
        return isMine;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public String getExtraInfo() {
        return extraInfo;
    }
}
