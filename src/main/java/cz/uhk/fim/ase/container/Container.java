package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.Listener;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.communication.impl.IceListener;
import cz.uhk.fim.ase.communication.impl.IceSender;
import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.agents.Agent;
import cz.uhk.fim.ase.model.ContainerEntity;
import cz.uhk.fim.ase.reporting.ReportManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Container extends LoggedObject {

    private Map<UUID, Agent> agents = new ConcurrentHashMap<UUID, Agent>();
    private ExecutorService executor;
    private Listener listener;
    private Sender sender = new IceSender();
    private ContainerEntity address;
    private MessagesQueue queue = new MessagesQueue();
    private ReportManager reportManager;

    public Container(String host, Integer port) {
        address = new ContainerEntity(host, port);
        reportManager = new ReportManager(this);
        resolveInstance();
        createThreadPool();
    }

    /**
     * ***************************** setters / getters *****************************
     */

    public ContainerEntity getAddress() {
        return address;
    }

    public Sender getSender() {
        return sender;
    }

    public Listener getListener() {
        return listener;
    }

    public MessagesQueue getQueue() {
        return queue;
    }

    public Map<UUID, Agent> getAgents() {
        return agents;
    }

    public void addAgent(Agent agent) {
        getLogger().info("Registering agent {}", agent.getEntity());
        agents.put(agent.getEntity().getId(), agent);
        Registry.get().register(agent);
    }

    public void removeAgent(UUID id) {
        getLogger().info("Removing agent with ID {}", id);
        if (agents.containsKey(id)) {
            agents.remove(id);
        }
    }

    /**
     * ***************************** logic *****************************
     */

    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerListener();
            }
        }).start();

        getLogger().info("Setting up container {}", getAddress());
        setup();
        while (true) {
            nextTick();
        }
    }

    abstract protected void setup();

    private void createThreadPool() {
        Integer poolSize = Config.get().concurrency.threadsPool;
        getLogger().debug("Creating threads pool with size {} on container {}", poolSize, getAddress());
        executor = Executors.newFixedThreadPool(poolSize);
    }

    private void resolveInstance() {
        String instance = discoverAnotherContainers();
        if (instance != null && Config.get().instance != null) {
            getLogger().warn("Using discovered instance ID {} (overriding configuration)", instance);
            Config.get().instance = instance;
            return;
        }
        if (instance != null) {
            Config.get().instance = instance;
        } else {
            if (Config.get().instance == null) {
                Config.get().instance = UUID.randomUUID().toString();
            }
        }
        getLogger().info("Creating new instance ID {}", Config.get().instance);
    }

    private String discoverAnotherContainers() {
        // TODO discover containers and instance ID
        return null;
    }

    private void registerListener() {
        getLogger().info("Creating listener on container {}", getAddress());
        listener = new IceListener(this);
        listener.listen();
    }

    private void nextTick() {
        getLogger().info("Executing tick #" + TickManager.get().getCurrentTick());

        Set<Callable<Object>> todoList = new HashSet<Callable<Object>>();
        for (Agent agent : agents.values()) {
            if (TickManager.get().isFinalTick()) {
                agent.takeDown();
            } else {
                Callable<Object> task = agent.getNextBehavior();
                if (task != null) {
                    todoList.add(task);
                }
            }
        }

        try {
            getLogger().debug("Waiting for agents");
            executor.invokeAll(todoList); // TODO: config
        } catch (InterruptedException e) {
            getLogger().error("Agents execution interrupted");
        } finally {
            getLogger().debug("Agents are done");
        }

        if (TickManager.get().isFinalTick()) {
            getLogger().debug("This is final tick, shutdown");
            return;
        }

        if (TickManager.get().isReportTick()) {
            getLogger().debug("Executing report for tick #" + TickManager.get().getCurrentTick());
            reportManager.doReport();
        }

        // cleanup
        for (Agent agent : agents.values()) {
            if (agent.isDone()) {
                agent.takeDown();
                agents.remove(agent.getEntity().getId());
                getLogger().warn("Agent {} is done, destroying", agent.getEntity());
            }
        }

        TickManager.get().setReadyState(); // TODO wait for other containers
    }
}
