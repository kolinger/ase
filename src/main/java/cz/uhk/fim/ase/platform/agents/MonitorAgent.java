package cz.uhk.fim.ase.platform.agents;

import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.platform.Monitor;
import cz.uhk.fim.ase.platform.ServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Special type of agent used for system monitoring. Exists only once in platform.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MonitorAgent extends Agent {

    public MonitorAgent(AgentEntity entity) {
        super(entity);
    }

    @Override
    public Map<String, String> getReportValues() {
        Monitor monitor = ServiceLocator.getMonitor();
        Map<String, String> values = new HashMap<String, String>();
        values.put("messageQueueSize", monitor.getQueueSize().toString());
        values.put("tasksCount", monitor.getTasksCount().toString());
        values.put("sentMessagesCount", monitor.getSentMessagesCount().toString());
        values.put("receivedMessagesCount", monitor.getReceivedMessagesCount().toString());
        values.put("freeMemory", monitor.getFreeMemory().toString());
        values.put("totalMemory", monitor.getTotalMemory().toString());
        values.put("platformAgentsCount", monitor.getPlatformAgentsCount().toString());
        values.put("totalAgentsCount", monitor.getTotalAgentsCount().toString());
        return values;
    }
}
