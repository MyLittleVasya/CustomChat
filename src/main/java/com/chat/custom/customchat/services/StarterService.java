package com.chat.custom.customchat.services;

import com.chat.custom.customchat.entities.User;
import com.chat.custom.customchat.repositories.ChatRepo;
import com.chat.custom.customchat.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;

@Service
public class StarterService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ChatRepo chatRepo;

    public void defaultInitialization(Model model, User user) {
        if (user != null)
        {
            user = userRepo.findById(user.getId());
            if (user.getChatList().size() != 0)
                model.addAttribute("chats", user.getChatList());
            if (user.getActiveChatList().size() != 0)
                model.addAttribute("activeChats", user.getActiveChatList());
        }
        model.addAttribute("users", userRepo.findAll());
    }

    public void initializeChatAndMessages(Model model, long id) {
        model.addAttribute("chat", chatRepo.findById(id));
        var messages = chatRepo.findById(id).getMessageList();
        if (messages.size() != 0)
            Collections.reverse(messages);
        model.addAttribute("messages", messages);
    }
}
