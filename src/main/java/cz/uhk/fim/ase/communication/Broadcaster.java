package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.model.AgentEntity;

import java.util.Set;

/**
 * Broadcaster can sent global messages to all nodes (including self).
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Broadcaster {

    void sendHello(Set<AgentEntity> agents);

    void sendSync();

    void sendBye();
}
