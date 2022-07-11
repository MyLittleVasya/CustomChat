package com.chat.custom.customchat.services;

import com.chat.custom.customchat.entities.User;
import com.chat.custom.customchat.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public String tryRegistrateUser(Map<String, String> form) {
        if (userRepo.findByUsername(form.get("username")) != null || userRepo.findByUsername(form.get("email")) != null)
        {
            return "Username or email is already corrupted!";
        }
        else
        {
            var user = new User(form.get("username"), passwordEncoder.encode(form.get("password")), form.get("email"), UUID.randomUUID().toString());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(username);
            message.setTo(user.getEmail());
            message.setSubject("Activate your account please. No Reply");
            message.setText(String.format(
                    "Hello, %s! \n" +
                            "Welcome to CustomChat app! \n"+
                            "Visit next link to activate your account \n"+
                            "http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()));
            mailSender.send(message);
            userRepo.save(user);
            return "The activation link was send to your email address.\nFollow instruction in letter to activate your account";
        }
    }

    public String tryActivateUser(String uuid) {
        var user = userRepo.findByActivationCode(uuid);
        if (user != null) {
            if (!user.isActivated()) {
                user.setActivated(true);
                userRepo.save(user);
                return "Your account was successfully activated!";
            }
            else {
                return "This account is already activated";
            }
        }
        else {
            return "User does not exist!";
        }
    }
}
