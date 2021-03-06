package client;


import java.net.URI;
import java.util.Scanner;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.Session;

import static util.JsonUtil.formatMessage;

public class Client {

    public static final String SERVER = "ws://omenproject.zapto.org:4444/ws/OMENchat";

    public static void main(String[] args) throws Exception {
        ClientManager client = ClientManager.createClient();
        String message;

        // connect to server
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tiny Chat!");
        System.out.println("What's your name?");
        String user = scanner.nextLine();
        Session session = client.connectToServer(ClientEndpoint.class, new URI(SERVER));
        System.out.println("You are logged in as: " + user);

        // repeatedly read a message and send it to the server (until quit)
        do {
            message = scanner.nextLine();
            session.getBasicRemote().sendText(formatMessage(message, user));
        } while (!message.equalsIgnoreCase("quit"));
    }

}
