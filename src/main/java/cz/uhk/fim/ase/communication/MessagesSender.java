package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.model.MessageEntity;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface MessagesSender {

    void send(MessageEntity message);
}
