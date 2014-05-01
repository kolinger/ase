package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.model.AgentEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Local agents registry.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */

public class Registry {

    final private List<AgentEntity> agents = new ArrayList<>();
    private List<AgentEntity> localIndex = new ArrayList<>();
    private Map<String, List<AgentEntity>> typeIndex = new HashMap<>();

    public void register(AgentEntity agent) {
        synchronized (agents) {
            if (!agents.contains(agent)) {
                if (agent.getNode().equals(ServiceLocator.getConfig().system.myAddress)) {
                    // local index
                    localIndex.add(agent);

                    // type index
                    String type = agent.getProperties().get("type");
                    List<AgentEntity> agentsList;
                    if (typeIndex.containsKey(type)) {
                        agentsList = typeIndex.get(type);
                    } else {
                        agentsList = new ArrayList<>();
                        typeIndex.put(type, agentsList);
                    }
                    agentsList.add(agent);
                }
                agents.add(agent);
            }
        }
    }

    public List<AgentEntity> getAgents() {
        return agents;
    }

    public AgentEntity getRandomByType(String type) {
        List<AgentEntity> agents = typeIndex.get(type);
        int size = agents.size();
        if (size == 0) {
            return null;
        }

        int randomIndex = new Random().nextInt(size);
        return agents.get(randomIndex);
    }

    public List<AgentEntity> getLocalAgents() {
        return localIndex;
    }
}
