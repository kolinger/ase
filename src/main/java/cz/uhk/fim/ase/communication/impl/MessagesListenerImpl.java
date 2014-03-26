package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.MessagesListener;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.container.Registry;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;
import org.zeromq.ZMQ;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesListenerImpl extends LoggedObject implements MessagesListener {

    private MessagesQueue queue;

    public void setQueue(MessagesQueue queue) {
        this.queue = queue;
    }

    public void listen(String address) {
        ZMQ.Socket listener = ContextHolder.getContext().socket(ZMQ.PULL);
        listener.bind("tcp://" + address);
        getLogger().info("Message listener listen on " + address);

        while (!Thread.currentThread().isInterrupted()) {
            byte[] bytes = listener.recv();
            byte type = bytes[0]; // first byte is message type

            if (type == 0) { // direct
                MessageEntity message = (MessageEntity) MessageConverter.convertBytesToObject(bytes);
                if (message != null) {
                    queue.addMessage(message);
                }
            } else if (type == 2) { // welcome
                WelcomeMessage welcomeMessage = (WelcomeMessage) MessageConverter.convertBytesToObject(bytes);
                if (welcomeMessage != null) {
                    for (AgentEntity agent : welcomeMessage.getAgents()) {
                        Registry.get().register(agent);
                    }
                    Registry.get().updateNode(welcomeMessage.getNode(), welcomeMessage.getTick());
                }
            }
        }

        listener.close();
    }
}
