package com.repositories;

import com.model.Message;

import java.util.List;

public interface MessageRepository extends Repository<Message>{
    List<Message> findAllInRoom(String title);
    Message getLastAuthorsMessage(String author);
}
