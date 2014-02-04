package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.container.agents.TestAgent;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class StaticContainer extends Container {

    public StaticContainer(String host, Integer port) {
        super(host, port);
    }

    protected void setup() {
        for (Integer count = 1; count <= 5; count++) {
            addAgent(new TestAgent(this));
        }
    }
}
