package com.app;

import com.client.Client;

import java.io.IOException;

public class main {
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 5432);
            client.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
