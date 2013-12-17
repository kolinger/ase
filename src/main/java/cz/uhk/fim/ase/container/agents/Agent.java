package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communatication.MessagesQueue;
import cz.uhk.fim.ase.communatication.Sender;
import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.container.agents.behaviours.Behavior;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Agent extends LoggedObject implements Runnable {

    private AgentEntity entity;
    private Container container;
    private List<Behavior> behaviors = new ArrayList<Behavior>();

    public Agent(Container container) {
        this.container = container;
        entity = new AgentEntity(UUID.randomUUID(), container.getAddress());
    }

    public AgentEntity getEntity() {
        return entity;
    }

    public void setEntity(AgentEntity entity) {
        this.entity = entity;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public void addBehaviour(Behavior behavior) {
        behaviors.add(behavior);
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public void run() {
        getLogger().debug("Setting up agent {} ", getEntity());
        setup();
        getLogger().debug("Processing agent {} ", getEntity());
        processBehaviours();
        getLogger().debug("Taking down agent {} ", getEntity());
        takeDown();
    }

    public void send(MessageEntity message) {
        getLogger().debug("Agent {} sending message {}", getEntity(), message);
        Sender sender = getContainer().getSender();
        sender.send(message);
    }

    public MessageEntity receive() {
        MessagesQueue queue = getContainer().getQueue();
        MessageEntity message = queue.search(getEntity().getId());
        getLogger().debug("Agent {} receiving first message {}", getEntity(), message);
        return message;
    }

    protected void setup() {
    }

    protected void takeDown() {
    }

    private void processBehaviours() {
        Boolean running;
        do {
            running = false;
            for (Behavior behavior : behaviors) {
                if (!behavior.isDone() && behavior.isRunnable()) {
                    behavior.run();
                    running = true;
                }
            }
        } while (running);
    }

    public class Task implements Runnable {

        private Behavior behavior;

        public Task(Behavior behavior) {
            this.behavior = behavior;
        }

        public void run() {
            behavior.run();
        }
    }
}
