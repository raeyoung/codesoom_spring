package com.codesom.demo;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetSocketAddress;

@SpringBootApplication
public class App {
    public String getGreeting() {
        return "Hello World!!!";
    }

    public static void main(String[] args) {
        // Java Web
//        System.out.println(new App().getGreeting());
//
//        try {
//            InetSocketAddress address = new InetSocketAddress(8000);
//            HttpServer httpServer = HttpServer.create(address, 0);
//
//            HttpHandler handler = new DemoHttpHandler();
//            httpServer.createContext("/", handler);
//            httpServer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        SpringApplication.run(App.class, args);
    }
}
