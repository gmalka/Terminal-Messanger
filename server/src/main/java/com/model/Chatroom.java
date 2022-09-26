package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chatroom {
    private Long id;
    private String owner;
    private String title;
    private List<Message> messages;

    public Chatroom(Long id, String owner, String title, List<Message> messages) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.messages = messages;
    }

    public Chatroom(String title, String owner) {
        this.title = title;
        this.owner = owner;
        this.messages = new ArrayList<Message>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chatroom chatroom = (Chatroom) o;
        return id == chatroom.id && Objects.equals(owner, chatroom.owner) && Objects.equals(title, chatroom.title) && Objects.equals(messages, chatroom.messages);
    }

    public void addMessage(Message newMessage) {
        messages.add(newMessage);
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
