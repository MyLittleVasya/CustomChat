package com.chat.custom.customchat.dto;

public class MessageDTO {
    private long id;
    private long sender;
    private String sender_username;

    private String chat_name;
    private long chat;
    private String text;

    private String date;

    public MessageDTO() {
    }

    public MessageDTO(long id, long sender, long chat, String text, String date) {
        this.id = id;
        this.sender = sender;
        this.chat = chat;
        this.text = text;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getChat() {
        return chat;
    }

    public void setChat(long chat) {
        this.chat = chat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender_username() {
        return sender_username;
    }

    public void setSender_username(String sender_username) {
        this.sender_username = sender_username;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }
}
