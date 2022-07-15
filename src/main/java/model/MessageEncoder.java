package model;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public String encode(final Message message) throws EncodeException {
        return Json.createObjectBuilder()
                .add("message", message.getContent())
                .add("sender", message.getSender())
                .add("received", "")
                .build().toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}