package com.chat.custom.customchat.controllers;

import com.chat.custom.customchat.dto.ChatDTO;
import com.chat.custom.customchat.dto.MessageDTO;
import com.chat.custom.customchat.entities.User;
import com.chat.custom.customchat.repositories.ChatRepo;
import com.chat.custom.customchat.repositories.MessageRepo;
import com.chat.custom.customchat.repositories.UserRepo;
import com.chat.custom.customchat.services.ChatService;
import com.chat.custom.customchat.services.MessageService;
import com.chat.custom.customchat.services.StarterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Collections;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ChatService chatService;

    @Autowired
    private StarterService starterService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String getMainPage(Model model, @AuthenticationPrincipal User user) {
        starterService.defaultInitialization(model, user);
        return "main";
    }

    @PostMapping("/createChat")
    public String createChat(@RequestParam Map<String, String> body, @RequestParam(name = "chat_image") File image, Model model, @AuthenticationPrincipal User user) {
        chatService.tryCreateChat(body, image, user);
        starterService.defaultInitialization(model, user);
        return "main";
    }



    @GetMapping("/chat/{id}")
    public String getChat(@PathVariable long id, Model model, @AuthenticationPrincipal User user) {
        starterService.defaultInitialization(model, user);
        starterService.initializeChatAndMessages(model, id);
        return "main";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public MessageDTO send(MessageDTO message) throws Exception {
        var response = messageService.dtoToMessage(message);
        messageService.trySaveMessage(response);
        message.setSender_username(userRepo.findById(message.getSender()).getUsername());
        message.setChat_name(chatRepo.findById(message.getChat()).getName());
        return message;
    }

    @MessageMapping("/addActiveChat")
    @Transactional
    public void addActiveChat(ChatDTO chatDTO) throws Exception {
        var user = userRepo.findById(chatDTO.getUserId());
        var chat = chatRepo.findById(chatDTO.getChatId());
        if (!user.getActiveChatList().contains(chat)) {
            user.getActiveChatList().add(chat);
        }
        userRepo.save(user);
    }

    @MessageMapping("/removeActiveChat")
    @Transactional
    public void removeActiveChat(ChatDTO chatDTO) throws Exception {
        var user = userRepo.findById(chatDTO.getUserId());
        var chat = chatRepo.findById(chatDTO.getChatId());
        if (user.getActiveChatList().contains(chat)) {
            user.getActiveChatList().remove(chat);
        }
        userRepo.save(user);
    }
}
