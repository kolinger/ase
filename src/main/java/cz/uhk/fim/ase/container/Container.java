package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.GlobalListener;
import cz.uhk.fim.ase.communication.GlobalSender;
import cz.uhk.fim.ase.communication.MessagesListener;
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
    private GlobalSender globalSender = new GlobalSenderImpl();
    private ReportManager reportManager;

    // agents
    private Map<String, Agent> agents = new ConcurrentHashMap<String, Agent>();
    private ExecutorService executor;

    // listeners
    private MessagesListener listener;
    private GlobalListener discoverListener;

    private String address;

    public Container(String host, Integer port) {
        address = host + ":" + port;
        reportManager = new ReportManager(this);
        globalSender.setAddress(Config.get().system.globalSenderAddress + ":" + Config.get().system.globalSenderPort);
        Config.get().system.id = UUID.randomUUID().toString();
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
                registerGlobalListener();
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
        Integer poolSize = Config.get().system.threadsPool;
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
        return globalSender.sendHello();
    }

    private void registerListener() {
        getLogger().info("Binding listener on container {}", getAddress());
        listener = new MessagesListenerImpl();
        listener.setQueue(queue);
        listener.listen(getAddress());
    }

    private void registerGlobalListener() {
        String address = Config.get().system.globalSenderAddress + ":" + Config.get().system.globalSenderPort;
        getLogger().info("Binding global listener on {}", address);
        discoverListener = new GlobalListenerImpl();
        discoverListener.setContainer(this);
        discoverListener.listen(address);
    }

    /**
     * Runs agents tasks
     */
    private Boolean nextTick() {
        TickManager.get().setReadyState(false);

        getLogger().info("Executing tick #" + TickManager.get().getCurrentTick());

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
        TickManager.get().setReadyState(true);
        while (true) {
            Long tick = globalSender.sendSync();
            if (tick == null || TickManager.get().getCurrentTick() <= tick) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return true;
    }

    private void shutdown() {
        executor.shutdownNow();

        // TODO: how to kill ice threads?
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet);
    }
}
