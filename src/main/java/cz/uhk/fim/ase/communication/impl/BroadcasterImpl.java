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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.util.Set;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class BroadcasterImpl implements Broadcaster {

    private static Logger logger = LoggerFactory.getLogger(Broadcaster.class);
    private ZMQ.Socket socket;

    public void sendSync() {
        logger.debug("Sending sync message");
        SyncMessage message = new SyncMessageImpl();
        message.setTick(ServiceLocator.getSyncService().getTick());
        message.setNode(ServiceLocator.getConfig().system.listenerAddress);

        ZMQ.Socket socket = getSocket();
        socket.send(MessagesConverter.convertObjectToBytes(message), 0);
        socket.close();
        this.socket = null;
    }

    public void sendHello(Set<AgentEntity> agents) {
        logger.debug("Sending hello message");
        HelloMessage message = new HelloMessageImpl();
        message.setTick(ServiceLocator.getSyncService().getTick());
        message.setNode(ServiceLocator.getConfig().system.listenerAddress);
        message.setAgents(agents);

        ZMQ.Socket socket = getSocket();
        socket.send(MessagesConverter.convertObjectToBytes(message), 0);
        socket.close();
        this.socket = null;
    }

    public void sendBye() {
        logger.debug("Sending bye message");
        SyncMessage message = new SyncMessageImpl();
        message.setNode(ServiceLocator.getConfig().system.listenerAddress);

        ZMQ.Socket socket = getSocket();
        socket.send(MessagesConverter.convertObjectToBytes(message), 0);
        socket.close();
        this.socket = null;
    }

    private ZMQ.Socket getSocket() {
        if (socket == null) {
            socket = ContextHolder.getContext().socket(ZMQ.DEALER);
            socket.connect("tcp://" + ServiceLocator.getConfig().system.broadcasterAddress);
        }
        return socket;
    }
}
