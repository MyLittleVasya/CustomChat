package com.chat.custom.customchat.services;

import com.chat.custom.customchat.entities.Chat;
import com.chat.custom.customchat.entities.User;
import com.chat.custom.customchat.repositories.ChatRepo;
import com.chat.custom.customchat.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ChatRepo chatRepo;

    public void tryCreateChat(Map<String, String> body, File image, User user) {
        var chat = new Chat();
        chat.setName(body.get("name"));
        chat.setImage(image);
        var setUsers = new HashSet<User>();
        var members_id = body.get("members_usernames").split("/");
        for (String username : members_id) {
            if (!StringUtils.isEmpty(username))
                setUsers.add(userRepo.findByUsername(username));
        }
        user = userRepo.findById(user.getId());
        setUsers.add(user);
        chat.setUserList(setUsers.stream().collect(Collectors.toList()));
        if (chat.getUserList().size() > 2)
            chat.setDialog(true);
        chatRepo.save(chat);
        for (User member: chat.getUserList()) {
            member.getChatList().add(chat);
            userRepo.save(member);
        }
    }
}
