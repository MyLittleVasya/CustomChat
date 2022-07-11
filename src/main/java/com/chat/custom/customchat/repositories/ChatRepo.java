package com.chat.custom.customchat.repositories;

import com.chat.custom.customchat.entities.Chat;
import com.chat.custom.customchat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findById(long id);
}
