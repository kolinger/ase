package cz.uhk.fim.virtualeconomy.container;

import cz.uhk.fim.virtualeconomy.common.LoggedObject;
import cz.uhk.fim.virtualeconomy.communatication.Listener;
import cz.uhk.fim.virtualeconomy.communatication.MessagesQueue;
import cz.uhk.fim.virtualeconomy.communatication.Sender;
import cz.uhk.fim.virtualeconomy.communatication.impl.IceListener;
import cz.uhk.fim.virtualeconomy.communatication.impl.IceSender;
import cz.uhk.fim.virtualeconomy.container.agents.Agent;
import cz.uhk.fim.virtualeconomy.model.ContainerEntity;
import org.ubercraft.statsd.StatsdCounter;
import org.ubercraft.statsd.StatsdLoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Container extends LoggedObject {

    private Map<UUID, Future> agents = new HashMap<UUID, Future>();
    private ExecutorService executor;
    private Listener listener;
    private Sender sender = new IceSender();
    private ContainerEntity address;
    private MessagesQueue queue = new MessagesQueue();
    private StatsdCounter agentsCounter = StatsdLoggerFactory.getLogger("statsd.virtualeconomy.agents.count");

    public Container(String host, Integer port) {
        address = new ContainerEntity(host, port);

        Integer poolSize = Integer.parseInt(App.getConfiguration().getProperty("concurrency.threads_pool"));
        getLogger().debug("Creating threads pool with size {} on container {}", poolSize, getAddress());
        executor = Executors.newFixedThreadPool(poolSize);
    }

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

    public void addAgent(Agent agent) {
        getLogger().info("Registering agent {}", agent.getEntity());
        agentsCounter.infoCount();
        Future future = executor.submit(agent);
        agents.put(agent.getEntity().getId(), future);
    }

    public void removeAgent(UUID id) {
        getLogger().info("Removing agent with ID {}", id);
        agentsCounter.infoCount(-1);
        if (agents.containsKey(id)) {
            Future future = agents.get(id);
            future.cancel(false);
            agents.remove(id);
        }
    }

    public void run() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                registerListener();
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getLogger().info("Setting up container {}", getAddress());
                setup();
            }
        });
    }

    abstract protected void setup();

    private void registerListener() {
        getLogger().info("Creating listener on container {}", getAddress());
        listener = new IceListener(this);
        listener.listen();
    }
}
