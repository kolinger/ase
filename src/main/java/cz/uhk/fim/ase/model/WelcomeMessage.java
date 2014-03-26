package cz.uhk.fim.ase.model;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface WelcomeMessage extends Serializable {

    String getNode();
    void setNode(String address);

    Long getTick();
    void setTick(Long tick);

    Set<AgentEntity> getAgents();
    void setAgents(Set<AgentEntity> agents);
}
