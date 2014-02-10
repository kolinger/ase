package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.container.agents.Agent;
import cz.uhk.fim.ase.container.agents.TestAgent1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class Registry extends LoggedObject {

    private List<Agent> agents = new ArrayList<Agent>();

    private static Registry instance;

    public static Registry get() {
        if (instance == null) {
            instance = new Registry();
        }
        return instance;
    }

    public void register(Agent agent) {
        agents.add(agent);
    }

    public Agent getRandomAgent1() {
        int size = agents.size();
        if (size == 0) {
            return null;
        }

        while (true) {
            int index = new Random().nextInt(size);
            if (agents.get(index) instanceof TestAgent1) {
                return agents.get(index);
            }
        }
    }
}
