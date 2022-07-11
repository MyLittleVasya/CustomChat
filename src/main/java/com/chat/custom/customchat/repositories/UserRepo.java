package com.chat.custom.customchat.repositories;

import com.chat.custom.customchat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByActivationCode(String uuid);

    User findById(long id);
}
