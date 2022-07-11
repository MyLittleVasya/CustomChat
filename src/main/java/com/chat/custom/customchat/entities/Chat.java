package com.chat.custom.customchat.entities;

import javax.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_table")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToMany
    private List<User> userList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> messageList = new ArrayList<>();

    private File image;

    private boolean dialog;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> users) {
        this.userList = users;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messages) {
        this.messageList = messages;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public boolean isDialog() {
        return dialog;
    }

    public void setDialog(boolean group) {
        this.dialog = group;
    }
}
