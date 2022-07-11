package com.chat.custom.customchat.services;

import com.chat.custom.customchat.dto.MessageDTO;
import com.chat.custom.customchat.entities.Message;
import com.chat.custom.customchat.repositories.ChatRepo;
import com.chat.custom.customchat.repositories.MessageRepo;
import com.chat.custom.customchat.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MessageService {

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    public Message dtoToMessage(MessageDTO messageDTO) {
        var message = new Message();
        message.setChat(chatRepo.findById(messageDTO.getChat()));
        message.setSender(userRepo.findById(messageDTO.getSender()));
        message.setText(messageDTO.getText());
        return message;
    }

    @Transactional
    public void trySaveMessage(Message response) {
        var chat = chatRepo.findById(response.getChat().getId());
        chat.getMessageList().add(response);
        messageRepo.save(response);
        chatRepo.save(chat);
    }
}
