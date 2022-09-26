package com.repositories;

import com.model.Chatroom;

public interface RoomRepository extends Repository<Chatroom> {
    Chatroom findByTitle(String title);
}
