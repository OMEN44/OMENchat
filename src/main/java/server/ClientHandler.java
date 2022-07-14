package server;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = bufferedReader.readLine();
            clientHandlers.add(this);

            //get external ip:
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = in.readLine(); //you get the IP as a String

            System.out.println("Client with username '" + username + "' connected from: " + ip);
            broadcastMessage(this.username + " joined");
        } catch (IOException e) {
            closeClient(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler cli : clientHandlers) {
            try {
                if (!cli.username.equals(this.username)) {
                    cli.bufferedWriter.write(message);
                    cli.bufferedWriter.newLine();
                    cli.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeClient(this.socket, this.bufferedReader, this.bufferedWriter);
                break;
            }
        }
    }

    public void closeClient(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        clientHandlers.remove(this);
        System.out.println("Client with username '" + username + "' disconnected.");
        broadcastMessage(username = " left");
        try{
            if(bufferedReader !=null){
                bufferedReader.close();
            }
            if(bufferedWriter !=null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (this.socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeClient(this.socket, this.bufferedReader, this.bufferedWriter);
                break;
            }
        }
    }
}
