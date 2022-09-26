package com.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;

    private ServerConnector serverConnector;

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        serverConnector = new ServerConnector(socket);
    }

    public void start() {
        try {
            serverConnector.start();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverConnector.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class ServerConnector {
        private Scanner reader, clientReader;
        private Socket socket;
        private PrintWriter writer;

        public ServerConnector(Socket socket) throws IOException {
            this.socket = socket;
            clientReader = new Scanner(System.in);
            reader = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
        }

        public void start() throws IOException, InterruptedException {
            String command, input;
            System.out.println(reader.nextLine());
            while (true) {
                System.out.println(reader.nextLine());
                command = clientReader.nextLine();
                switch (command.toLowerCase()) {
                    case "registration":
                        writer.println("registration");
                        startRegister();
                        break;
                    case "enter":
                        writer.println("enter");
                        startEnter();
                        break;
                    case "exit":
                        writer.println("exit");
                        System.out.println(reader.nextLine());
                        close();
                    default:
                        Thread.sleep(1000);
                        writer.println("nothing");
                }
            }
        }

        private void startEnter() throws InterruptedException {
            System.out.println("Enter username:");
            writer.println(clientReader.nextLine());
            System.out.println("Enter password:");
            writer.println(clientReader.nextLine());
            if (reader.nextLine().equalsIgnoreCase("ok1")) {
                System.out.println(reader.nextLine());
                enterService();
            } else System.out.println(reader.nextLine());
        }

        private void enterService() throws InterruptedException {
            String command;

            while (true) {
                System.out.println(reader.nextLine());
                command = clientReader.nextLine();
                switch (command.toLowerCase()) {
                    case "enter":
                        writer.println("enter");
                        enterRoom();
                        break;
                    case "create":
                        writer.println("create");
                        createRoom();
                        break;
                    case "getall":
                        writer.println("getall");
                        getAllRooms();
                        break;
                    case "exit":
                        writer.println("exit");
                        System.out.println(reader.nextLine());
                        return;
                    default:
                        writer.println("nothing");
                        Thread.sleep(500);
                        System.out.println("Please enter command");
                }
            }
        }


        private void getAllRooms() {
            String command = reader.nextLine();
            if (command.equalsIgnoreCase("ok1"))
                System.out.println(reader.nextLine());
            else {
                int i = 0;
                String rooms = reader.nextLine();
                for (String str : rooms.split(" "))
                    System.out.println(++i + " --- " + str);
            }
        }

        private void createRoom() {
            System.out.println("Enter new rooms name or BACK for back");
            String command = clientReader.nextLine();
            writer.println(command);
            switch (reader.nextLine().toLowerCase()) {
                case "ok1":
                    System.out.println(reader.nextLine());
                    break;
                case "ok2":
                    return;
                case "er1":
                    System.out.println("Room with name " + command + " is already exist");
                    break;
            }
        }

        private void enterRoom() {
            String request;

            if (reader.nextLine().equals("er1")) {
                System.out.println("No created rooms");
                return;
            }
            System.out.println("Enter Chatroom name");
            writer.println(clientReader.nextLine());
            request = reader.nextLine();
            System.out.println(request);
            if (request.startsWith("Welcome")) {
                letsTalk();
            }
        }

        private void letsTalk() {
            MessagesFromServer messagesFromServer = new MessagesFromServer();

            messagesFromServer.start();
            String message;
            do {
                message = clientReader.nextLine();
                writer.println(message);
            } while (!message.equalsIgnoreCase("back"));
            try {
                messagesFromServer.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private class MessagesFromServer extends Thread {
            @Override
            public void run() {
                String message;
                try {
                    while (true) {
                        message = reader.nextLine();
                        if (message.equalsIgnoreCase("back")) {
                            System.err.println(reader.nextLine());
                            break;
                        }
                        System.out.println(message);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        private void startRegister() {
            System.out.println("REGISTRATION NEW USER\n\tEnter User name:");
            writer.println(clientReader.nextLine());
            System.out.println("Enter password:");
            writer.println(clientReader.nextLine());
            if (reader.nextLine().equalsIgnoreCase("er1"))
                System.out.println("User already exists");
            else
                System.out.println(reader.nextLine());
        }

        public void close() throws IOException {
            clientReader.close();
            writer.close();
            reader.close();
            socket.close();
            System.exit(0);
        }
    }
}
