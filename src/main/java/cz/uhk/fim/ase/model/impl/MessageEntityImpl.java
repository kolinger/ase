package cz.uhk.fim.ase.model.impl;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Just wrapper for generated entity from slice, because we need remove dependencies on Ice.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessageEntityImpl extends LoggedObject implements MessageEntity {

    private AgentEntity sender;
    private List<AgentEntity> receivers = new ArrayList<AgentEntity>();
    private Integer type;
    private String content;

    public static cz.uhk.fim.ase.model.internal.MessageEntity convert(MessageEntity message) {
        cz.uhk.fim.ase.model.internal.MessageEntity converted = new cz.uhk.fim.ase.model.internal.MessageEntity();
        converted.sender = AgentEntityImpl.convert(message.getSender());
        converted.receivers = new cz.uhk.fim.ase.model.internal.AgentEntity[message.getReceivers().size()];
        Integer index = 0;
        for (AgentEntity agent : message.getReceivers()) {
            converted.receivers[index] = AgentEntityImpl.convert(agent);
        }
        converted.type = message.getType();
        converted.content = message.getContent();
        return converted;
    }

    public static MessageEntity createResponse(MessageEntity message) {
        MessageEntity response = new MessageEntityImpl();
        response.getReceivers().add(message.getSender());
        return response;
    }

    public MessageEntityImpl(cz.uhk.fim.ase.model.internal.MessageEntity message) {
        sender = new AgentEntityImpl(message.sender);
        for (cz.uhk.fim.ase.model.internal.AgentEntity agent : message.receivers) {
            receivers.add(new AgentEntityImpl(agent));
        }
        type = message.type;
        content = message.content;
    }

    public MessageEntityImpl() {
        // blank
    }

    public AgentEntity getSender() {
        return sender;
    }

    public void setSender(AgentEntity sender) {
        this.sender = sender;
    }

    public List<AgentEntity> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<AgentEntity> receivers) {
        this.receivers = receivers;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
