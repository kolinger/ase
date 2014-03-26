package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.GlobalSender;
import cz.uhk.fim.ase.model.HelloMessage;
import cz.uhk.fim.ase.model.SyncMessage;
import org.zeromq.ZMQ;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class GlobalSenderImpl extends LoggedObject implements GlobalSender {

    private Map<String, ZMQ.Socket> sockets = new HashMap<String, ZMQ.Socket>();

    public void sendSync(SyncMessage message, String node) {
        getLogger().debug("Sending message {}", message);

        ZMQ.Socket socket = getSocket(node);
        socket.send(MessageConverter.convertObjectToBytes(message));
    }

    public void sendHello(HelloMessage message, String node) {
        getLogger().debug("Sending message {}", message);

        ZMQ.Socket socket = getSocket(node);
        socket.send(MessageConverter.convertObjectToBytes(message));
    }

    private ZMQ.Socket getSocket(String address) {
        if (!sockets.containsKey(address)) {
            ZMQ.Socket socket = ContextHolder.getContext().socket(ZMQ.DEALER);
            sockets.put(address, socket);
        }
        return sockets.get(address);
    }
}
