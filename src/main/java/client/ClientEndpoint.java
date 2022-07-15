package client;

import model.Message;
import model.MessageDecoder;
import model.MessageEncoder;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.text.SimpleDateFormat;

import static java.lang.String.format;

@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ClientEndpoint {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(format("Connection established. session id: %s", session.getId()));
    }

    @OnMessage
    public void onMessage(Message message) {
        System.out.println(format("[%s:%s] %s", simpleDateFormat.format(message.getReceived()), message.getSender(), message.getContent()));
    }

}
