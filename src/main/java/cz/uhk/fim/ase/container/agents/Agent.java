package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.container.agents.behaviours.Behavior;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.reporting.model.ReportAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Agent extends LoggedObject implements ReportAgent {

    private AgentEntity entity;
    private Container container;
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    private Integer behaviorsPointer = 0;
    private Boolean done = false;

    public Agent(Container container) {
        this.container = container;
        entity = new AgentEntity(UUID.randomUUID(), container.getAddress());
        setup();
    }

    public String getReportId() {
        return entity.getId().toString();
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

    public Boolean isDone() {
        return done;
    }

    public void addBehaviour(Behavior behavior) {
        behaviors.add(behavior);
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public Behavior getNextBehavior() {
        // no tasks, we are done
        if (behaviors.size() == 0) {
            done = true;
            return null;
        }

        // too high pointer, back to start
        if (behaviorsPointer >= behaviors.size()) {
            behaviorsPointer = 0;
        }

        // remove task when is done
        Behavior behavior = behaviors.get(behaviorsPointer++);
        if (behavior.isDone()) {
            behaviors.remove(behavior);
        }
        return behavior;
    }

    protected void setup() {
        // virtual
    }

    public void takeDown() {
        // virtual
    }

    protected void send(MessageEntity message) {
        getLogger().debug("Agent {} sending message {}", getEntity(), message);
        Sender sender = getContainer().getSender();
        sender.send(message);
    }

    protected MessageEntity receive() {
        MessagesQueue queue = getContainer().getQueue();
        MessageEntity message = queue.search(getEntity().getId());
        getLogger().debug("Agent {} receiving first message {}", getEntity(), message);
        return message;
    }
}
