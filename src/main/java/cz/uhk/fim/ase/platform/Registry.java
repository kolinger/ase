package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.model.AgentEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Local agents registry.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */

public class Registry {

    private List<AgentEntity> agents = new ArrayList<AgentEntity>();

    private static Registry instance;

    public static Registry get() {
        if (instance == null) {
            instance = new Registry();
        }
        return instance;
    }

    public void register(AgentEntity agent) {
        if (!agents.contains(agent)) {
            agents.add(agent);
        }
    }

    public List<AgentEntity> getAgents() {
        return agents;
    }

    public AgentEntity getRandomAgent1() {
        int size = agents.size();
        if (size == 0) {
            return null;
        }

        while (true) {
            int index = new Random().nextInt(size);
            if (agents.get(index).getProperties().get("type").equals("1")) {
                return agents.get(index);
            }
        }
    }
}
