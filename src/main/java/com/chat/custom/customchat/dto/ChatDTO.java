package com.chat.custom.customchat.dto;

public class ChatDTO {
    long chatId;
    String chatName;

    long userId;

    public ChatDTO(long chatId, String chatName, long userId) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.userId = userId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
