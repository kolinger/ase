package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.model.HelloMessage;
import cz.uhk.fim.ase.model.SyncMessage;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface GlobalSender {

    void sendHello(HelloMessage message, String node);
    void sendSync(SyncMessage message, String node);
}
