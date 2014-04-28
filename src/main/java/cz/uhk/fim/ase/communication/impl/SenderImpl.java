package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.communication.impl.helpers.ContextHolder;
import cz.uhk.fim.ase.communication.impl.helpers.MessagesConverter;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class SenderImpl implements Sender {

    private Map<String, ZMQ.Socket> sockets = new HashMap<String, ZMQ.Socket>();

    public void send(MessageEntity message) {
        LoggerFactory.getLogger(SenderImpl.class).debug("Sending message " + message);
        byte[] bytes = MessagesConverter.convertObjectToBytes(message);

        for (AgentEntity receiver : message.getReceivers()) {
            ZMQ.Socket socket = getSocket(receiver.getNode());
            socket.send(bytes, 0);
        }
    }

    public void sendWelcome(WelcomeMessage message, String node) {
        ZMQ.Socket socket = getSocket(node);
        socket.send(MessagesConverter.convertObjectToBytes(message), 0);
    }

    private ZMQ.Socket getSocket(String address) {
        if (!sockets.containsKey(address)) {
            ZMQ.Socket socket = ContextHolder.getContext().socket(ZMQ.PUSH);
            socket.connect("tcp://" + address);
            sockets.put(address, socket);
        }
        return sockets.get(address);
    }
}
