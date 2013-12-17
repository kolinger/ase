package cz.uhk.fim.ise.communatication;

import cz.uhk.fim.ise.common.LoggedObject;
import cz.uhk.fim.ise.model.MessageEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;


/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesQueue extends LoggedObject {

    private Map<UUID, Queue<MessageEntity>> queue = new HashMap<UUID, Queue<MessageEntity>>();

    public synchronized void addMessage(MessageEntity message) {
        for (MessageEntity.Address receiver : message.getReceivers()) {
            Queue<MessageEntity> messages;
            if (queue.containsKey(receiver.getAgentId())) {
                messages = queue.get(receiver.getAgentId());
                messages.add(message);
            } else {
                messages = new LinkedList<MessageEntity>();
                messages.add(message);
                queue.put(receiver.getAgentId(), messages);
            }
        }
    }

    public synchronized MessageEntity search(UUID agentId) {
        if (queue.containsKey(agentId)) {
            Queue<MessageEntity> messages = queue.get(agentId);
            return messages.poll();
        }
        return null;
    }

    public synchronized MessageEntity searchByType(UUID agentId, MessageEntity.Type type) {
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
