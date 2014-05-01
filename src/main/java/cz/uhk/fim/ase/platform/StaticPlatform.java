package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.common.NamedThreadFactory;
import cz.uhk.fim.ase.model.impl.AgentEntityImpl;
import cz.uhk.fim.ase.platform.agents.BenchmarkAgent;
import cz.uhk.fim.ase.platform.agents.TestAgent1;
import cz.uhk.fim.ase.platform.agents.TestAgent2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class StaticPlatform extends Platform {

    private Integer agentsCount = ServiceLocator.getConfig().environment.agentsCount;

    protected void setup() {
        int threadsCount = ServiceLocator.getConfig().system.registrationThreadsCount;

        ExecutorService executor = Executors.newFixedThreadPool(threadsCount,
                new NamedThreadFactory("registration-worker-thread"));

        List<Callable<Object>> tasks = new ArrayList<>();
        for (int count = 1; count <= threadsCount; count++) {
            tasks.add(new Callable<Object>() {
                @Override
                public Object call() {
                    boolean next = false;
                    while (agentsCount > 0) {
                        if (ServiceLocator.getConfig().environment.benchmarkMode) {
                            addAgent(new BenchmarkAgent(AgentEntityImpl.create("benchmark")));
                            increaseCount();
                        } else {
                            if (next) {
                                addAgent(new TestAgent1(AgentEntityImpl.create("1")));
                            } else {
                                addAgent(new TestAgent2(AgentEntityImpl.create("2")));
                            }
                            next = !next;
                            increaseCount();
                        }
                    }

                    return null;
                }
            });
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            // ignore
        }
        executor.shutdown();
    }

    private synchronized void increaseCount() {
        agentsCount--;
    }
}
