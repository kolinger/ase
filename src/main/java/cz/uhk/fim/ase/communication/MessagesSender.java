package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface MessagesSender {

    void send(MessageEntity message);
    void sendWelcome(WelcomeMessage message, String node);
}
