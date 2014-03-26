package cz.uhk.fim.ase.model;

import java.io.Serializable;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface SyncMessage extends Serializable {

    String getNode();
    void setNode(String address);

    Long getTick();
    void setTick(Long tick);
}
