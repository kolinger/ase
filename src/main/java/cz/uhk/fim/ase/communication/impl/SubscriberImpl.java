package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.communication.Subscriber;
import cz.uhk.fim.ase.communication.impl.helpers.ContextHolder;
import cz.uhk.fim.ase.communication.impl.helpers.MessagesConverter;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.HelloMessage;
import cz.uhk.fim.ase.model.SyncMessage;
import cz.uhk.fim.ase.model.WelcomeMessage;
import cz.uhk.fim.ase.model.impl.WelcomeMessageImpl;
import cz.uhk.fim.ase.platform.Registry;
import cz.uhk.fim.ase.platform.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.util.HashSet;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class SubscriberImpl implements Subscriber {

    private Logger logger = LoggerFactory.getLogger(SubscriberImpl.class);
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            bind();
        }
    }, "subscriber-thread");
    private String myself = ServiceLocator.getConfig().system.myAddress;

    public void shutdown() {
        thread.interrupt();
    }

    public void subscribe() {
        thread.start();
    }

    private void bind() {
        String address = ServiceLocator.getConfig().system.subscriberAddress;

        ZMQ.Socket subscriber = ContextHolder.getContext().socket(ZMQ.SUB);
        subscriber.connect("tcp://" + address);
        subscriber.subscribe("".getBytes()); // subscribe to all
        logger.info("Subscribed to " + address);

        while (!Thread.currentThread().isInterrupted()) {
            byte[] bytes = subscriber.recv(0);
            if (bytes == null || bytes.length == 0) {
                continue;
            }
            handleBytes(bytes);
        }

        subscriber.close();
    }

    private void handleBytes(byte[] bytes) {
        byte type = bytes[0]; // first byte is message type

        if (type == 1) { // hello
            HelloMessage message = (HelloMessage) MessagesConverter.convertBytesToObject(bytes);
            if (message != null && !message.getNode().equals(myself)) {
                for (AgentEntity agent : message.getAgents()) {
                    Registry.get().register(agent);
                }
                logger.debug("Received hello message from " + message.getNode());
                ServiceLocator.getSyncService().updateNodeState(message.getNode(), message.getTick());

                WelcomeMessage response = new WelcomeMessageImpl();
                response.setAgents(new HashSet<AgentEntity>(Registry.get().getAgents()));
                response.setNode(myself);
                response.setTick(ServiceLocator.getSyncService().getTick());
                ServiceLocator.getSender().sendWelcome(response, message.getNode());
            }
        } else if (type == 3) { // sync
            SyncMessage message = (SyncMessage) MessagesConverter.convertBytesToObject(bytes);
            if (message != null && !message.getNode().equals(myself)) {
                logger.debug("Received sync message from " + message.getNode());
                ServiceLocator.getSyncService().updateNodeState(message.getNode(), message.getTick());
            }
        }
    }
}
