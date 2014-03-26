package cz.uhk.fim.ase.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface AgentEntity extends Serializable {

    String getContainer();
    void setContainer(String address);

    String getId();
    void setId(String id);

    Map<String, String> getProperties();
    void setProperties(Map<String, String> properties);
}
