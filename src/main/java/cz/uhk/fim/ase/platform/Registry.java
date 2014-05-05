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

    final private List<AgentEntity> mainIndex = new ArrayList<>();
    private List<AgentEntity> localIndex = new ArrayList<>();
    private List<AgentEntity> foreignIndex = new ArrayList<>();
    private Map<String, Map<String, List<AgentEntity>>> parameterIndex = new HashMap<>();
    private Map<String, Map<String, List<AgentEntity>>> parameterLocalIndex = new HashMap<>();
    private Map<String, Map<String, List<AgentEntity>>> parameterForeignIndex = new HashMap<>();

    public void register(AgentEntity agent) {
        synchronized (mainIndex) {
            if (!mainIndex.contains(agent)) {
                if (agent.getNode().equals(ServiceLocator.getConfig().system.myAddress)) { // local indexes
                    localIndex.add(agent);
                    addToParameterIndex(agent, parameterLocalIndex);
                } else { // foreign indexes
                    foreignIndex.add(agent);
                    addToParameterIndex(agent, parameterForeignIndex);
                }

                // mixed indexes
                addToParameterIndex(agent, parameterIndex);
                mainIndex.add(agent);
            }
        }
    }

    public List<AgentEntity> getAgents() {
        return mainIndex;
    }

    public List<AgentEntity> getLocalAgents() {
        return localIndex;
    }

    public List<AgentEntity> getForeignAgents() {
        return foreignIndex;
    }

    public AgentEntity findRandomByParameter(Map<String, Map<String, List<AgentEntity>>> index, String key, String value) {
        if (index.containsKey(key)) {
            Map<String, List<AgentEntity>> wrapper = index.get(key);
            if (wrapper.containsKey(value)) {
                List<AgentEntity> items = wrapper.get(value);
                if (items.size() > 0) {
                    int randomIndex = new Random().nextInt(items.size());
                    return items.get(randomIndex);
                }
            }
        }
        return null;
    }

    public AgentEntity findRandomByParameter(String key, String value) {
        return findRandomByParameter(parameterIndex, key, value);
    }

    public AgentEntity findRandomByType(String type) {
        return findRandomByParameter("type", type);
    }

    public AgentEntity findRandomLocalByType(String type) {
        return findRandomByParameter(parameterLocalIndex, "type", type);
    }

    public AgentEntity findRandomForeignByType(String type) {
        return findRandomByParameter(parameterForeignIndex, "type", type);
    }

    private void addToParameterIndex(AgentEntity agent, Map<String, Map<String, List<AgentEntity>>> index) {
        for (Map.Entry<String, String> entry : agent.getProperties().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            Map<String, List<AgentEntity>> wrapper;
            if (!index.containsKey(key)) {
                wrapper = new HashMap<String, List<AgentEntity>>();
                index.put(key, wrapper);
            } else {
                wrapper = index.get(key);
            }

            List<AgentEntity> items;
            if (!wrapper.containsKey(value)) {
                items = new ArrayList<AgentEntity>();
                wrapper.put(value, items);
            } else {
                items = wrapper.get(value);
            }

            items.add(agent);
        }
    }
}
