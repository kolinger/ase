package cz.uhk.fim.ase.model.impl;

import cz.uhk.fim.ase.model.ByeMessage;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ByeMessageImpl implements ByeMessage {

    private String node;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
