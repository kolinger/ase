package cz.uhk.fim.ase.model.impl;

import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;

import java.util.Set;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class WelcomeMessageImpl implements WelcomeMessage {

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
