package cz.uhk.fim.ase.communication;

import slices.Message;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Sender {

    void send(Message message);
}
