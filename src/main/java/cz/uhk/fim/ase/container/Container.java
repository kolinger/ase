package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.GlobalListener;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.MessagesSender;
import cz.uhk.fim.ase.communication.impl.GlobalListenerImpl;
import cz.uhk.fim.ase.communication.impl.GlobalSenderImpl;
import cz.uhk.fim.ase.communication.impl.MessagesListenerImpl;
import cz.uhk.fim.ase.communication.impl.MessagesQueueImpl;
import cz.uhk.fim.ase.communication.impl.MessagesSenderImpl;
import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.agents.Agent;
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
 * Main object with runs everything and also used as service locator
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Container extends LoggedObject {

    // services
    private MessagesSender sender = new MessagesSenderImpl();
    private MessagesQueue queue = new MessagesQueueImpl();
    private ReportManager reportManager;

    // agents
    private Map<String, Agent> agents = new ConcurrentHashMap<String, Agent>();
    private ExecutorService executor;

    // listeners
    private MessagesListenerImpl listener;
    private GlobalListener discoverListener;

    private String address;

    public Container(String host, Integer port) {
        address = host + ":" + port;
        reportManager = new ReportManager(this);
        resolveInstance();
        createThreadPool();
    }

    /**
     * ***************************** setters / getters *****************************
     */

    public String getAddress() {
        return address;
    }

    public MessagesSender getSender() {
        return sender;
    }

    public MessagesQueue getQueue() {
        return queue;
    }

    public Map<String, Agent> getAgents() {
        return agents;
    }

    public void addAgent(Agent agent) {
        getLogger().debug("Registering agent {}", agent.getEntity());
        agents.put(agent.getEntity().getId(), agent);
        Registry.get().register(agent.getEntity());
    }

    /**
     * ***************************** logic *****************************
     */

    public void run() {
        // run listener for messages (in background)
        Thread listenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                registerListener();
            }
        });
        listenerThread.start();

        // run listener for discover request (in background)
        Thread discoverListenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                registerDiscoverListener();
            }
        });
        discoverListenerThread.start();

        // setup and execute agents tasks
        getLogger().info("Setting up container {}", getAddress());
        setup();
        while (true) {
            if (!nextTick()) {
                break;
            }
        }
    }

    abstract protected void setup();

    /**
     * Create a thread pool used for agents tasks only
     */
    private void createThreadPool() {
        Integer poolSize = Config.get().concurrency.threadsPool;
        getLogger().debug("Creating threads pool with size {} on container {}", poolSize, getAddress());
        executor = Executors.newFixedThreadPool(poolSize);
    }

    /**
     * Create discover request and resolve instance ID - use external instance ID if exists otherwise create new ID
     * Only one ID exists at time
     */
    private void resolveInstance() {
        String instance = discoverAnotherContainers();
        if (instance != null) {
            getLogger().warn("Using discovered instance ID {} (overriding configuration)", instance);
            Config.get().instance = instance;
        } else {
            if (Config.get().instance == null) {
                Config.get().instance = UUID.randomUUID().toString();
            }
        }
        getLogger().info("Creating new instance ID {}", Config.get().instance);
    }

    private String discoverAnotherContainers() {
        getLogger().info("Discovering containers on {}", "239.255.1.1:10000");
        GlobalSenderImpl client = new GlobalSenderImpl();
        return client.process(Config.get().container.discoverAddress, Config.get().container.discoverPort);
    }

    private void registerListener() {
        getLogger().info("Creating listener on container {}", getAddress());
        listener = new MessagesListenerImpl();
        listener.setQueue(queue);
        listener.listen(getAddress());
    }

    private void registerDiscoverListener() {
        getLogger().info("Creating discover listener on {}", "239.255.1.1:10000");
        discoverListener = new GlobalListenerImpl();
        discoverListener.setContainer(this);
        discoverListener.listen(Config.get().container.discoverAddress + ":" + Config.get().container.discoverPort);
    }

    /**
     * Runs agents tasks
     */
    private Boolean nextTick() {
        getLogger().debug("Executing tick #" + TickManager.get().getCurrentTick());

        // collect tasks
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

        // run tasks
        try {
            getLogger().debug("Waiting for agents");
            executor.invokeAll(todoList);
        } catch (InterruptedException e) {
            getLogger().error("Agents execution interrupted");
        } finally {
            getLogger().debug("Agents are done");
        }

        // if this is last task, shutdown executing
        if (TickManager.get().isFinalTick()) {
            getLogger().info("This is final tick, shutdown");
            shutdown();
            return false;
        }

        // if this is report tick, create report
        if (TickManager.get().isReportTick()) {
            getLogger().info("Executing report for tick #" + TickManager.get().getCurrentTick());
            reportManager.doReport();
        }

        // wait for other containers then continue to next tick
        TickManager.get().setReadyState(); // TODO wait for other containers
        return true;
    }

    private void shutdown() {
        executor.shutdownNow();

        // TODO: how to kill ice threads?
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet);
    }
}
