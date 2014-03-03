package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.model.MessageType;
import slices.AgentEntity;
import slices.Message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesQueue extends LoggedObject {

    private Map<String, Queue<Message>> queue = new HashMap<String, Queue<Message>>();

    public synchronized void addMessage(Message message) {
        for (AgentEntity receiver : message.receivers) {
            Queue<Message> messages;
            if (queue.containsKey(receiver.id)) {
                messages = queue.get(receiver.id);
                messages.add(message);
            } else {
                messages = new LinkedList<Message>();
                messages.add(message);
                queue.put(receiver.id, messages);
            }
        }
    }

    public synchronized Message search(String agentId) {
        if (queue.containsKey(agentId)) {
            Queue<Message> messages = queue.get(agentId);
            return messages.poll();
        }
        return null;
    }

    public synchronized Message searchByType(String agentId, MessageType type) {
        if (queue.containsKey(agentId)) {
            Queue<Message> messages = queue.get(agentId);
            for (Message message : messages) {
                if (message.type == type.ordinal()) {
                    messages.remove(message);
                    return message;
                }
            }
        }
        return null;
    }
}
