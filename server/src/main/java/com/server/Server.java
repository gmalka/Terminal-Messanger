package com.server;

import com.model.Chatroom;
import com.model.User;
import com.services.RoomServices;
import com.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class Server {

    private ServerSocket serverSocket;
    private RoomServices roomservices;
    private UserServices userServices;

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private List<Person> clients = new ArrayList<>();

    @Autowired
    public Server(UserServices usersService, RoomServices roomsService) {
        this.userServices = usersService;
        this.roomservices = roomsService;
    }

    public void run(int port) {
        try {
            serverSocket = new ServerSocket(port);
            ServerMonitor monitor = new ServerMonitor();
            monitor.start();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                Person person = new Person(socket);
                clients.add(person);
                person.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void messageToChat(String message, String name, String room) {
        userServices.sendMessage(message, name, room);
        clients.stream().filter(title -> Objects.equals(title.room, room)).forEach(sender -> sender.writer.println("[" + LocalDateTime.now().format(FORMATTER) + "] " + name + ": " + message));
    }

    private void stopServer() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.exit(0);
    }

    private class ServerMonitor extends Thread {
        Scanner scanner = new Scanner(System.in);

        @Override
        public void run() {
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equalsIgnoreCase("exit")) {
                    Server.this.stopServer();
                }
            }
        }
    }

    private class Person extends Thread {
        private Socket socket;
        private Scanner scanner;
        private PrintWriter writer;
        private String room, password, name;

        Person(Socket socket) {
            this.socket = socket;
            try {
                scanner = new Scanner(socket.getInputStream());
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            writer.println("Welcome to chat");
            try {
                while (true) {
                    writer.println("Please enter command: Registration, Enter, exit");
                    String str = scanner.nextLine();
                    System.out.println(str);
                    if ("registration".equalsIgnoreCase(str)) {
                        signUpUser();
                    } else if ("enter".equalsIgnoreCase(str)) {
                        if (signInUser())
                            enterTheRoom();
                    } else if ("exit".equalsIgnoreCase(str)) {
                        System.out.println(name + " logged out");
                        writer.println(name + " log out");
                        return;
                    }
                }
            } catch (Exception ignored) {System.out.println(name + " logged out"); }
        }

        public void enterTheRoom() {
            while (true) {
                writer.println("Hello mr " + name + ", please print ENTER for enter the room, create for CREATE room or GETALL for get all rooms. Enter EXIT for exit");
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("enter"))
                    enterRoom();
                else if (command.equalsIgnoreCase("create"))
                    createRoom();
                else if (command.equalsIgnoreCase("getall"))
                    returnAllRooms();
                else if (command.equalsIgnoreCase("exit")) {
                    System.out.println(name + " log out");
                    writer.println(name + " log out");
                    name = "guest";
                    return;
                }
            }
        }

        public void createRoom() {
            String chatName = scanner.nextLine();
            if (chatName.equalsIgnoreCase("back")) {
                writer.println("Ok2 ");
                return;
            }
            try {
                roomservices.createNewRoom(new Chatroom(chatName, name));
                System.out.println(name + " create room " + chatName);
                writer.println("Ok1\n" + name + " create room " + chatName);
            } catch (Exception e) {
                writer.println("Er1");
            }
        }

        public void returnAllRooms() {
            List<Chatroom> list = roomservices.getAllRooms();
            if (list == null || list.isEmpty()) {
                writer.println("Ok1\nNo created rooms");
                return;
            }
            writer.println("Ok2");
            StringBuilder sb = new StringBuilder();
            System.out.println(room);
            for (Chatroom room : list) {
                sb.append(room.getTitle());
                sb.append(" ");
            }
            writer.println(sb);
        }

        public void enterRoom() {
            List<Chatroom> list = roomservices.getAllRooms();
            if (list == null || list.isEmpty()) {
                writer.println("er1");
                return;
            }
            writer.println("ok1");
            String chatName = scanner.nextLine();
            Chatroom chatroom = list.stream().filter(room -> Objects.equals(room.getTitle(), chatName)).findAny().orElse(null);
            if (chatroom != null) {
                room = chatName;
                System.out.println("User '" + name + "' in room: " + chatName);
                writer.println("Welcome to " + chatName + ", enter BACK to leave");
                letsTalk();
            } else {
                writer.println("Can't find " + chatName);
            }
        }

        public void letsTalk() {
            messageToChat(name + " enter the chat " + room, name, room);
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("back")) {
                    System.out.println(name + " left room " + room);
                    writer.println("back");
                    messageToChat(name + " left room " + room, name, room);
                    room = "";
                    return;
                }
                messageToChat(message, name, room);
            }
        }

        public boolean signInUser() {
            try {
                name = scanner.nextLine();
                password = scanner.nextLine();
            } catch (Exception e) {
                writer.println("Er1");
                return false;
            }
            if (clients.stream().filter(client -> client != this && Objects.equals(client.name, name)).findAny().orElse(null) != null) {
                writer.println("Er1");
                System.out.println("Authorization failed for user: " + name + ", user already in system");
                writer.println("Authorization failed for user: " + name + ", user already in system");
                name = "guest";
                return false;
            }
            if (userServices.signIn(name, password)) {
                writer.println("Ok1");
                System.out.println("Authorization successful for user: " + name);
                writer.println("Authorization successful for user: " + name);
                return true;
            } else {
                writer.println("Er1");
                System.out.println("Authorization failed for user: " + name);
                writer.println("Authorization failed for user: " + name);
                name = "guest";
                return false;
            }
        }

        public void signUpUser() {
            try {
                name = scanner.nextLine();
                password = scanner.nextLine();
                userServices.signUp(new User(null, name, password));
                name = "guest";
            } catch (Exception e) {
                writer.println("er1");
                return ;
            }
            writer.println("ok1");
            writer.println("Success registration, mr " + name);
        }
    }
}
