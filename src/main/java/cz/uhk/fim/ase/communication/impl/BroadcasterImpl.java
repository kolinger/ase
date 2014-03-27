package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.communication.Broadcaster;
import cz.uhk.fim.ase.communication.impl.helpers.ContextHolder;
import cz.uhk.fim.ase.communication.impl.helpers.MessagesConverter;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.HelloMessage;
import cz.uhk.fim.ase.model.SyncMessage;
import cz.uhk.fim.ase.model.impl.HelloMessageImpl;
import cz.uhk.fim.ase.model.impl.SyncMessageImpl;
import cz.uhk.fim.ase.platform.ServiceLocator;
import org.zeromq.ZMQ;

import java.util.Set;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class BroadcasterImpl implements Broadcaster {

    private ZMQ.Socket socket;

    public void sendSync() {
        SyncMessage message = new SyncMessageImpl();
        message.setTick(ServiceLocator.getSyncService().getTick());
        message.setNode(ServiceLocator.getConfig().system.listenerAddress);

        ZMQ.Socket socket = getSocket();
        socket.send(MessagesConverter.convertObjectToBytes(message));
    }

    public void sendHello(Set<AgentEntity> agents) {
        HelloMessage message = new HelloMessageImpl();
        message.setTick(ServiceLocator.getSyncService().getTick());
        message.setNode(ServiceLocator.getConfig().system.listenerAddress);
        message.setAgents(agents);

        ZMQ.Socket socket = getSocket();
        socket.send(MessagesConverter.convertObjectToBytes(message));
    }

    public void sendBye() {
        SyncMessage message = new SyncMessageImpl();
        message.setNode(ServiceLocator.getConfig().system.listenerAddress);

        ZMQ.Socket socket = getSocket();
        socket.send(MessagesConverter.convertObjectToBytes(message));
    }

    private ZMQ.Socket getSocket() {
        if (socket == null) {
            socket = ContextHolder.getContext().socket(ZMQ.DEALER);
            socket.connect("tcp://" + ServiceLocator.getConfig().system.broadcasterAddress);
        }
        return socket;
    }
}
