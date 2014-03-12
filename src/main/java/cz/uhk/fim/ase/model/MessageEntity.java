package cz.uhk.fim.ase.model;

import java.util.List;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface MessageEntity {

    AgentEntity getSender();
    void setSender(AgentEntity sender);

    List<AgentEntity> getReceivers();
    void setReceivers(List<AgentEntity> receivers);

    Integer getType();
    void setType(Integer type);

    String getContent();
    void setContent(String content);
}
