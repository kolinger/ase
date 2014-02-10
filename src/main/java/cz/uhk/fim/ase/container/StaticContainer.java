package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.container.agents.TestAgent1;
import cz.uhk.fim.ase.container.agents.TestAgent2;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class StaticContainer extends Container {

    public StaticContainer(String host, Integer port) {
        super(host, port);
    }

    protected void setup() {
        for (Integer count = 1; count <= 500000; count++) {
            addAgent(new TestAgent1(this));
            addAgent(new TestAgent2(this));
        }
    }
}
