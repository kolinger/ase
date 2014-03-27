package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;

/**
 * Sender can sent messages to another node. Is used to send welcome messages and agent's direct messages.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Sender {

    void send(MessageEntity message);

    void sendWelcome(WelcomeMessage message, String node);
}
