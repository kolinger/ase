package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.agents.TestAgent1;
import cz.uhk.fim.ase.container.agents.TestAgent2;
import slices.AgentEntity;

import java.util.HashMap;
import java.util.UUID;

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
            AgentEntity entity1 = new AgentEntity();
            entity1.id = UUID.randomUUID().toString();
            entity1.container = getAddress();
            entity1.properties = new HashMap<String, String>();
            entity1.properties.put("type", "1");
            addAgent(new TestAgent1(entity1, getSender(), getQueue()));

            AgentEntity entity2 = new AgentEntity();
            entity2.id = UUID.randomUUID().toString();
            entity2.container = getAddress();
            entity2.properties = new HashMap<String, String>();
            entity2.properties.put("type", "2");
            addAgent(new TestAgent2(entity2, getSender(), getQueue()));
        }
    }
}
