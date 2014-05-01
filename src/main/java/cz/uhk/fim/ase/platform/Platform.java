package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.common.NamedThreadFactory;
import cz.uhk.fim.ase.communication.Listener;
import cz.uhk.fim.ase.communication.impl.SenderImpl;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.impl.AgentEntityImpl;
import cz.uhk.fim.ase.platform.agents.Agent;
import cz.uhk.fim.ase.platform.agents.MonitorAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Platform {

    private Logger logger = LoggerFactory.getLogger(Platform.class);

    private Map<String, Agent> agents = new ConcurrentHashMap<String, Agent>();
    private ExecutorService executor;
    private Monitor monitor = ServiceLocator.getMonitor();
    private SenderImpl sender = (SenderImpl) ServiceLocator.getSender();

    public Platform() {
        executor = Executors.newFixedThreadPool(
                ServiceLocator.getConfig().system.computeThreads,
                new NamedThreadFactory("agent-compute-thread"));
    }

    /**
     * ***************************** setters / getters *****************************
     */

    public void addAgent(Agent agent) {
        agents.put(agent.getEntity().getId(), agent);
        ServiceLocator.getRegistry().register(agent.getEntity());
    }

    /**
     * ***************************** logic *****************************
     */

    public void run() {
        // start services
        ServiceLocator.getSubscriber().subscribe();
        Listener listener = ServiceLocator.getListener();
        listener.listen();

        // wait for listener initialization
        while (!listener.isReady()) {
            // wait
        }

        // send hello message
        Set<AgentEntity> agentsEntities = new HashSet<AgentEntity>();
        for (Agent agent : agents.values()) {
            agentsEntities.add(agent.getEntity());
        }
        ServiceLocator.getBroadcaster().sendHello(agentsEntities);

        // setup and execute agents tasks
        logger.info("Setting up platform");
        if (ServiceLocator.getConfig().environment.monitorAgent) {
            AgentEntity monitorAgentEntity = new AgentEntityImpl();
            monitorAgentEntity.setId("monitor-agent");
            monitorAgentEntity.setNode(ServiceLocator.getConfig().system.myAddress);
            Agent monitorAgent = new MonitorAgent(monitorAgentEntity);
            addAgent(monitorAgent);
        }
        setup();
        while (nextTick()) {
            // run agents, run!
        }
    }

    abstract protected void setup();

    /**
     * Runs agents tasks
     */
    private Boolean nextTick() {
        SyncService syncService = ServiceLocator.getSyncService();

        logger.info("Executing tick #" + syncService.getTick());

        // collect tasks
        Set<Callable<Object>> todoList = new HashSet<Callable<Object>>();
        for (Agent agent : agents.values()) {
            Callable<Object> task = agent.getNextBehavior();
            if (task != null) {
                todoList.add(task);
            }
        }

        // run tasks
        try {
            logger.debug("Waiting for agents");
            monitor.increaseTasksCount(todoList.size());
            executor.invokeAll(todoList);
        } catch (InterruptedException e) {
            logger.error("Agents execution interrupted");
        } finally {
            logger.debug("Agents are done");
        }
        while (sender.getQueue().size() > 0) {
            // wait
        }

        // if this is report tick, create report
        if (syncService.isReportTick()) {
            logger.info("Executing report for tick #" + syncService.getTick());
            ServiceLocator.getReportService().doReport(agents);
        }

        // if this is last tick, shutdown
        if (syncService.isFinalTick()) {
            logger.info("This is final tick, shutdown");
            shutdown();
            return false;
        }

        // wait for other containers then continue to next tick
        logger.debug("Tick is done - now wait for others platforms");
        syncService.waitForOthers();
        return true;
    }

    private void shutdown() {
        // stop execution
        executor.shutdownNow();
        ServiceLocator.getSubscriber().shutdown();
        ServiceLocator.getListener().shutdown();

        // send bye message
        ServiceLocator.getBroadcaster().sendBye();
    }
}
