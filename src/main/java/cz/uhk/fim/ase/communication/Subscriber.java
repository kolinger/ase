package cz.uhk.fim.ase.communication;

/**
 * Subscriber listening for broadcasts.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Subscriber {

    void subscribe();

    void shutdown();
}
