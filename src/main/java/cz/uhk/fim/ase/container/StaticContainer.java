package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.agents.TestAgent1;
import cz.uhk.fim.ase.container.agents.TestAgent2;
import cz.uhk.fim.ase.model.impl.AgentEntityImpl;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class StaticContainer extends Container {

    public StaticContainer(String host, Integer port) {
        super(host, port);
    }

    protected void setup() {
        Integer max = Config.get().container.agentsCount / 2;
        for (Integer count = 1; count <= max; count++) {
            addAgent(new TestAgent1(AgentEntityImpl.create(getAddress(), "1"), getSender(), getQueue()));
            addAgent(new TestAgent2(AgentEntityImpl.create(getAddress(), "2"), getSender(), getQueue()));
        }
    }
}
