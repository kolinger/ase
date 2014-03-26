package cz.uhk.fim.ase.communication;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface GlobalListener {

    void setNode(String node);
    void setMessagesSender(MessagesSender sender);

    void listen(String address);
}
