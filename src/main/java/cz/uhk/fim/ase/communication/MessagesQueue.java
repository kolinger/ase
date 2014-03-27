package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.model.MessageEntity;

/**
 * MessagesQueue holds messages for agents. Other messages are processed immediately.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface MessagesQueue {

    void addMessage(MessageEntity message);

    MessageEntity search(String agentId);

    MessageEntity searchByType(String agentId, Integer type);
}
