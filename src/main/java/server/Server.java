package server;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("localhost", 4444, "/ws", ServerEndpoint.class);

        try {
            server.start();
            System.out.println("type help to show a list of commands.");
            System.out.println("type 'quit' and press enter to stop the server...");
            boolean running = true;
            while (running) {
                switch (new Scanner(System.in).nextLine().toLowerCase()) {
                    case "quit":
                        running = false;
                        break;
                    case "help":
                        System.out.println("Commands for OMENchat server\n=======================");
                        System.out.println("list   - Shows a list of all the currently connected users");
                        System.out.println("quit   - Ends this instance of the server kicking all the connected users");
                        System.out.println("=======================");
                        break;
                    case "list":
                        System.out.print("Number of connected users: ");
                        System.out.println(ServerEndpoint.peers.size());
                        System.out.println("=======================");
                        for (Session s : ServerEndpoint.peers) {
                            System.out.println(s.getId());
                        }
                        System.out.println("=======================");
                        break;
                }
            }
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

}