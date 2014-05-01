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
    private List<AgentEntity> foreignIndex = new ArrayList<>();
    private Map<String, List<AgentEntity>> typeIndex = new HashMap<>();
    private Map<String, List<AgentEntity>> typeLocalIndex = new HashMap<>();
    private Map<String, List<AgentEntity>> typeForeignIndex = new HashMap<>();

    public void register(AgentEntity agent) {
        synchronized (agents) {
            if (!agents.contains(agent)) {
                List<AgentEntity> agentsList;

                // local and foreign index
                if (agent.getNode().equals(ServiceLocator.getConfig().system.myAddress)) {
                    localIndex.add(agent);

                    // type index
                    String type = agent.getProperties().get("type");
                    if (typeLocalIndex.containsKey(type)) {
                        agentsList = typeLocalIndex.get(type);
                    } else {
                        agentsList = new ArrayList<>();
                        typeLocalIndex.put(type, agentsList);
                    }
                    agentsList.add(agent);
                } else {
                    foreignIndex.add(agent);

                    // type index
                    String type = agent.getProperties().get("type");
                    if (typeForeignIndex.containsKey(type)) {
                        agentsList = typeForeignIndex.get(type);
                    } else {
                        agentsList = new ArrayList<>();
                        typeForeignIndex.put(type, agentsList);
                    }
                    agentsList.add(agent);
                }

                // type index
                String type = agent.getProperties().get("type");
                if (typeIndex.containsKey(type)) {
                    agentsList = typeIndex.get(type);
                } else {
                    agentsList = new ArrayList<>();
                    typeIndex.put(type, agentsList);
                }
                agentsList.add(agent);

                // all
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

    public AgentEntity getRandomLocalByType(String type) {
        List<AgentEntity> agents = typeLocalIndex.get(type);
        int size = agents.size();
        if (size == 0) {
            return null;
        }

        int randomIndex = new Random().nextInt(size);
        return agents.get(randomIndex);
    }

    public AgentEntity getRandomForeignByType(String type) {
        List<AgentEntity> agents = typeForeignIndex.get(type);
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

    public List<AgentEntity> getForeignAgents() {
        return foreignIndex;
    }
}
