package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.platform.agents.TestAgent1;
import cz.uhk.fim.ase.platform.agents.TestAgent2;
import cz.uhk.fim.ase.model.impl.AgentEntityImpl;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class StaticPlatform extends Platform {

    protected void setup() {
        Integer max = ServiceLocator.getConfig().environment.agentsCount / 2;
        for (Integer count = 1; count <= max; count++) {
            addAgent(new TestAgent1(AgentEntityImpl.create("1")));
            addAgent(new TestAgent2(AgentEntityImpl.create("2")));
        }
    }
}
