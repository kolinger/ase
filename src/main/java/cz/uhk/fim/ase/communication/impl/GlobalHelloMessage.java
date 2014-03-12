package cz.uhk.fim.ase.communication.impl;

import Ice.Current;
import cz.uhk.fim.ase.communication.impl.internal._GlobalHelloMessageDisp;
import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.container.agents.Agent;
import cz.uhk.fim.ase.model.impl.AgentEntityImpl;
import cz.uhk.fim.ase.model.internal.AgentEntity;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class GlobalHelloMessage extends _GlobalHelloMessageDisp {

    private Container container;

    public GlobalHelloMessage(Container container) {
        this.container = container;
    }

    @Override
    public String getInstanceId(Current __current) {
        return Config.get().instance;
    }

    @Override
    public AgentEntity[] getAgents(Current __current) {
        AgentEntity[] agents = new AgentEntity[container.getAgents().size()];
        Integer index = 0;
        for (Agent agent : container.getAgents().values()) {
            agents[index] = AgentEntityImpl.convert(agent.getEntity());
            index++;
        }
        return agents;
    }
}
