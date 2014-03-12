package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


/**
 * Simple queue for holding messages mapped by agent's ID.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesQueueImpl extends LoggedObject implements MessagesQueue {

    private Map<String, Queue<MessageEntity>> queue = new HashMap<String, Queue<MessageEntity>>();

    public synchronized void addMessage(MessageEntity message) {
        for (AgentEntity receiver : message.getReceivers()) {
            Queue<MessageEntity> messages;
            if (queue.containsKey(receiver.getId())) {
                messages = queue.get(receiver.getId());
                messages.add(message);
            } else {
                messages = new LinkedList<MessageEntity>();
                messages.add(message);
                queue.put(receiver.getId(), messages);
            }
        }
    }

    public synchronized MessageEntity search(String agentId) {
        if (queue.containsKey(agentId)) {
            Queue<MessageEntity> messages = queue.get(agentId);
            return messages.poll();
        }
        return null;
    }

    public synchronized MessageEntity searchByType(String agentId, Integer type) {
        if (queue.containsKey(agentId)) {
            Queue<MessageEntity> messages = queue.get(agentId);
            for (MessageEntity message : messages) {
                if (message.getType() == type) {
                    messages.remove(message);
                    return message;
                }
            }
        }
        return null;
    }
}
