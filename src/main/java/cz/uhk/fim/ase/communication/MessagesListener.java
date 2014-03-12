package cz.uhk.fim.ase.communication;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface MessagesListener {

    void setQueue(MessagesQueue queue);

    void listen(String address);
}
