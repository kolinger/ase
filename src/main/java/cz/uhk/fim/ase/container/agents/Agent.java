package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.container.agents.behaviours.Behavior;
import cz.uhk.fim.ase.model.MessageType;
import slices.AgentEntity;
import slices.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Agent extends LoggedObject {

    private AgentEntity entity;
    private Sender sender;
    private MessagesQueue queue;
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    private Integer behaviorsPointer = 0;
    private Boolean done = false;

    public Agent(AgentEntity entity, Sender sender, MessagesQueue queue) {
        this.sender = sender;
        this.queue = queue;
        this.entity = entity;
        setup();
    }

    public AgentEntity getEntity() {
        return entity;
    }

    public void setEntity(AgentEntity entity) {
        this.entity = entity;
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

    abstract public Map<String, String> getReportValues();

    protected void send(Message message) {
        sender.send(message);
    }

    protected Message receive() {
        return queue.search(getEntity().id);
    }

    protected Message receive(MessageType type) {
        return queue.searchByType(getEntity().id, type);
    }
}
