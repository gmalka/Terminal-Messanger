package com.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Message {

    private static  final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private String message;
    private String author;
    private String titleRoom;
    private LocalDateTime time;

    public Message(String name, LocalDateTime time) {
        this.message = name;
        this.time = time;
    }

    public Message(String message, String author, String titleRoom, LocalDateTime time) {
        this.message = message;
        this.author = author;
        this.titleRoom = titleRoom;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(this.message, message.message) && Objects.equals(author, message.author) && Objects.equals(titleRoom, message.titleRoom) && Objects.equals(time, message.time);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitleRoom() {
        return titleRoom;
    }

    public void setTitleRoom(String titleRoom) {
        this.titleRoom = titleRoom;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", author='" + author + '\'' +
                ", titleRoom='" + titleRoom + '\'' +
                ", time=" + time.format(FORMATTER) +
                '}';
    }
}
