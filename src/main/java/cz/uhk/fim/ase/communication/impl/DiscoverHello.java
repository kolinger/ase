package cz.uhk.fim.ase.communication.impl;

import Ice.Current;
import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.container.agents.Agent;
import slices.AgentEntity;
import slices._DiscoverHelloDisp;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class DiscoverHello extends _DiscoverHelloDisp {

    private Container container;

    public DiscoverHello(Container container) {
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
            agents[index] = agent.getEntity();
            index++;
        }
        return agents;
    }
}
