package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.communication.Listener;
import cz.uhk.fim.ase.communication.impl.helpers.ContextHolder;
import cz.uhk.fim.ase.communication.impl.helpers.MessagesConverter;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;
import cz.uhk.fim.ase.platform.Registry;
import cz.uhk.fim.ase.platform.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ListenerImpl implements Listener {

    private Boolean ready = false;
    private Logger logger = LoggerFactory.getLogger(ListenerImpl.class);
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            bind();
        }
    });

    public void shutdown() {
        thread.interrupt();
    }

    @Override
    public synchronized Boolean isReady() {
        return ready;
    }

    public void listen() {
        thread.start();
    }

    private void bind() {
        String address = ServiceLocator.getConfig().system.listenerAddress;

        ZMQ.Socket listener = ContextHolder.getContext().socket(ZMQ.PULL);
        listener.bind("tcp://" + address);
        logger.info("Listen on " + address);

        ready = true;

        while (!Thread.currentThread().isInterrupted()) {
            byte[] bytes = listener.recv();
            handleBytes(bytes);
        }

        listener.close();
    }

    private void handleBytes(byte[] bytes) {
        byte type = bytes[0]; // first byte is message type

        if (type == 0) { // direct
            MessageEntity message = (MessageEntity) MessagesConverter.convertBytesToObject(bytes);
            if (message != null) {
                ServiceLocator.getMessagesQueue().addMessage(message);
            }
        } else if (type == 2) { // welcome
            WelcomeMessage welcomeMessage = (WelcomeMessage) MessagesConverter.convertBytesToObject(bytes);
            if (welcomeMessage != null) {
                for (AgentEntity agent : welcomeMessage.getAgents()) {
                    Registry.get().register(agent);
                }
                ServiceLocator.getSyncService().updateNodeState(welcomeMessage.getNode(), welcomeMessage.getTick());
            }
        }
    }
}
