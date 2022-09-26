package com.services;

import com.model.Chatroom;

import java.util.List;

public interface RoomServices {
    public void createNewRoom(Chatroom room);
    List<Chatroom> getAllRooms();
}
