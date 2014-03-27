package cz.uhk.fim.ase.reporting.model;

import cz.uhk.fim.ase.platform.agents.Agent;

import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Model {

    void save(Map<String, ? extends Agent> agents);
}
