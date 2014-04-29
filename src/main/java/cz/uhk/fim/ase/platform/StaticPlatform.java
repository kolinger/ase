package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.platform.agents.TestAgent1;
import cz.uhk.fim.ase.platform.agents.TestAgent2;
import cz.uhk.fim.ase.model.impl.AgentEntityImpl;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class StaticPlatform extends Platform {

    protected void setup() {
        Thread[] threads = new Thread[10];
        final Integer max = ServiceLocator.getConfig().environment.agentsCount / 2 / 10;
        for (int index = 0; index < 10; index++) {
            threads[index] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Integer count = 1; count <= max; count++) {
                        addAgent(new TestAgent1(AgentEntityImpl.create("1")));
                        addAgent(new TestAgent2(AgentEntityImpl.create("2")));
                    }
                }
            });
            threads[index].setName("agent-registration-thread-" + (index + 1));
            threads[index].start();
        }

        boolean done = false;
        while (!done) {
            done = true;
            for (int index = 0; index < 10; index++) {
                if (threads[index].isAlive()) {
                    done = false;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        }
    }
}
