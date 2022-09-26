package com.app;

import com.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class main {
    public static void main(String[] args) {
        try {
            //int port = Integer.parseInt(args[0].substring(7));
            ApplicationContext context = new AnnotationConfigApplicationContext("com.*");
            Server server = context.getBean(Server.class);
            server.run(5432);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
