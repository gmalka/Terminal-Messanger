package com.services;

import com.model.Chatroom;
import com.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomServiceReal implements RoomServices{
    private RoomRepository roomRepository;

    @Autowired
    public RoomServiceReal(RoomRepository roomRepository) { this.roomRepository = roomRepository; }

    @Override
    public void createNewRoom(Chatroom room) {
        if (roomRepository.findByTitle(room.getTitle()) != null)
            throw new RuntimeException("Room is already exist");
        roomRepository.add(room);
    }

    @Override
    public List<Chatroom> getAllRooms() {
        return roomRepository.getAll();
    }
}
