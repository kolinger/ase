package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communatication.Listener;
import cz.uhk.fim.ase.communatication.MessagesQueue;
import cz.uhk.fim.ase.communatication.Sender;
import cz.uhk.fim.ase.communatication.impl.IceListener;
import cz.uhk.fim.ase.communatication.impl.IceSender;
import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.agents.Agent;
import cz.uhk.fim.ase.model.ContainerEntity;

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

    public Container(String host, Integer port) {
        address = new ContainerEntity(host, port);

        Integer poolSize = Config.get().concurrency.threadsPool;
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
        Future future = executor.submit(agent);
        agents.put(agent.getEntity().getId(), future);
    }

    public void removeAgent(UUID id) {
        getLogger().info("Removing agent with ID {}", id);
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
