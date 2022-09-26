package com.services;

import com.model.Message;
import com.model.User;
import com.repositories.MessageRepository;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class UserServiceReal implements UserServices{

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private MessageRepository messageRepository;

    @Autowired
    public UserServiceReal(PasswordEncoder passwordEncoder, UserRepository userRepository, MessageRepository messageRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public boolean signIn(String username, String password) {
        User user = userRepository.getUserByName(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void signUp(User user) {
        if (userRepository.getUserByName(user.getName()) != null)
            throw new RuntimeException("User " + user.getName() + " is already exist");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.add(user);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByName(username);
    }

    @Override
    public void sendMessage(String message, String author, String title) {
        messageRepository.add(new Message(message, author, title, java.time.LocalDateTime.now()));
    }

    @Override
    public List<Message> getAllMessageByTitle(String title) {
        return messageRepository.findAllInRoom(title);
    }

    @Override
    public Message findLastRoom(String author) {
        return messageRepository.getLastAuthorsMessage(author);
    }
}
