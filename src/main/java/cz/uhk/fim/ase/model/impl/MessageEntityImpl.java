package cz.uhk.fim.ase.model.impl;

import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessageEntityImpl implements MessageEntity {

    private AgentEntity sender;
    private List<AgentEntity> receivers = new ArrayList<AgentEntity>();
    private Integer type;
    private String content;

    public static MessageEntity createResponse(MessageEntity message) {
        MessageEntity response = new MessageEntityImpl();
        response.getReceivers().add(message.getSender());
        return response;
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

    @Override
    public String toString() {
        return "MessageEntityImpl{" +
                "sender=" + sender +
                ", receivers=" + receivers +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
