package com.services;

import com.model.Message;
import com.model.User;

import java.util.List;

public interface UserServices {
    boolean signIn(String username, String password);
    void signUp(User user);
    void deleteUser(String username);
    void sendMessage(String message, String author, String title);
    List<Message> getAllMessageByTitle(String title);

    Message findLastRoom(String author);
}
