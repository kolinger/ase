package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.GlobalListener;
import cz.uhk.fim.ase.communication.GlobalSender;
import cz.uhk.fim.ase.communication.MessagesSender;
import cz.uhk.fim.ase.container.Registry;
import cz.uhk.fim.ase.container.TickManager;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.HelloMessage;
import cz.uhk.fim.ase.model.SyncMessage;
import cz.uhk.fim.ase.model.WelcomeMessage;
import cz.uhk.fim.ase.model.impl.WelcomeMessageImpl;
import org.zeromq.ZMQ;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class GlobalListenerImpl extends LoggedObject implements GlobalListener {

    private MessagesSender messagesSender;
    private String node;

    public void setMessagesSender(MessagesSender messagesSender) {
        this.messagesSender = messagesSender;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void listen(String address) {
        ZMQ.Socket subscriber = ContextHolder.getContext().socket(ZMQ.SUB);
        subscriber.connect("tcp://" + address);
        subscriber.subscribe("".getBytes()); // subscribe to all
        getLogger().info("Global listener subscribed to " + address);

        while (!Thread.currentThread().isInterrupted()) {
            byte[] bytes = subscriber.recv();
            byte type = bytes[0]; // first byte is message type

            if (type == 1) { // hello
                HelloMessage message = (HelloMessage) MessageConverter.convertBytesToObject(bytes);
                if (message != null && !message.getNode().equals(node)) {
                    for (AgentEntity agent : message.getAgents()) {
                        Registry.get().register(agent);
                    }
                    Registry.get().updateNode(message.getNode(), message.getTick());

                    WelcomeMessage response = new WelcomeMessageImpl();
                    response.setAgents(Registry.get().getAgents());
                    response.setNode(node);
                    response.setTick(TickManager.get().getCurrentTick());
                    messagesSender.sendWelcome(response, message.getNode());
                }
            } else if (type == 3) { // sync
                SyncMessage message = (SyncMessage) MessageConverter.convertBytesToObject(bytes);
                if (message != null && !message.getNode().equals(node)) {
                    Registry.get().updateNode(message.getNode(), message.getTick());
                }
            }
        }

        subscriber.close();
    }
}
