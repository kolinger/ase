package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.model.MessageEntity;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Sender {

    void send(MessageEntity message);
}
