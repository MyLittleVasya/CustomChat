package com.chat.custom.customchat.repositories;

import com.chat.custom.customchat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
