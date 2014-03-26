package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.MessagesSender;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;
import org.zeromq.ZMQ;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesSenderImpl extends LoggedObject implements MessagesSender {

    private Map<String, ZMQ.Socket> sockets = new HashMap<String, ZMQ.Socket>();

    public void send(MessageEntity message) {
        getLogger().debug("Sending message {}", message);

        byte[] bytes = MessageConverter.convertObjectToBytes(message);

        for (AgentEntity receiver : message.getReceivers()) {
            ZMQ.Socket socket = getSocket(receiver.getContainer());
            socket.send(bytes);
        }
    }

    public void sendWelcome(WelcomeMessage message, String node) {
        getLogger().debug("Sending message {}", message);

        ZMQ.Socket socket = getSocket(node);
        socket.send(MessageConverter.convertObjectToBytes(message));
    }

    private ZMQ.Socket getSocket(String address) {
        if (!sockets.containsKey(address)) {
            ZMQ.Socket socket = ContextHolder.getContext().socket(ZMQ.PUSH);
            sockets.put(address, socket);
        }
        return sockets.get(address);
    }
}
