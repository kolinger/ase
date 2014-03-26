package cz.uhk.fim.ase.model.impl;

import cz.uhk.fim.ase.model.SyncMessage;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class SyncMessageImpl implements SyncMessage {

    private String node;
    private Long tick;

    public Long getTick() {
        return tick;
    }

    public void setTick(Long tick) {
        this.tick = tick;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
