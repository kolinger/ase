package cz.uhk.fim.ase.model;

import java.io.Serializable;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface ByeMessage extends Serializable {

    String getNode();

    void setNode(String address);
}
