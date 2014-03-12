package cz.uhk.fim.ase.model.impl;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.model.AgentEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Just wrapper for generated entity from slice, because we need remove dependencies on Ice.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class AgentEntityImpl extends LoggedObject implements AgentEntity {

    private String container;
    private String id;
    private Map<String, String> properties = new HashMap<String, String>();

    public static AgentEntity create(String container, String type) {
        AgentEntity entity = new AgentEntityImpl();
        entity.setContainer(container);
        entity.setId(UUID.randomUUID().toString());
        entity.getProperties().put("type", type);
        return entity;
    }

    public static cz.uhk.fim.ase.model.internal.AgentEntity convert(AgentEntity agent) {
        cz.uhk.fim.ase.model.internal.AgentEntity converted = new cz.uhk.fim.ase.model.internal.AgentEntity();
        converted.container = agent.getContainer();
        converted.id = agent.getId();
        converted.properties = agent.getProperties();
        return converted;
    }

    public AgentEntityImpl() {
        // blank
    }

    public AgentEntityImpl(cz.uhk.fim.ase.model.internal.AgentEntity agent) {
        container = agent.container;
        id = agent.id;
        properties = agent.properties;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
