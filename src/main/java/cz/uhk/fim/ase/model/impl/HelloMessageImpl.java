package cz.uhk.fim.ase.model.impl;

import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.HelloMessage;

import java.util.Set;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class HelloMessageImpl implements HelloMessage {

    private String node;
    private Set<AgentEntity> agents;
    private Long tick;

    public Long getTick() {
        return tick;
    }

    public void setTick(Long tick) {
        this.tick = tick;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Set<AgentEntity> getAgents() {
        return agents;
    }

    public void setAgents(Set<AgentEntity> agents) {
        this.agents = agents;
    }
}
