package cz.uhk.fim.ase.platform.agents;

import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.platform.ServiceLocator;
import cz.uhk.fim.ase.platform.agents.behaviours.Behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Agent {

    private AgentEntity entity;
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    private Integer behaviorsPointer = 0;
    private Boolean done = false;

    public Agent(AgentEntity entity) {
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

    protected void send(MessageEntity message) {
        ServiceLocator.getSender().send(message);
    }

    protected MessageEntity receive() {
        return ServiceLocator.getMessagesQueue().search(getEntity().getId());
    }

    protected MessageEntity receive(Integer type) {
        return ServiceLocator.getMessagesQueue().searchByType(getEntity().getId(), type);
    }
}
