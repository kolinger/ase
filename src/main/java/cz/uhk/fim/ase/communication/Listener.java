package cz.uhk.fim.ase.communication;

/**
 * Listener subscribe for messages sent by Sender.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Listener {

    void listen();

    void shutdown();

    Boolean isReady();
}
