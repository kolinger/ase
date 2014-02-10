package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.model.MessageEntity;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class IceHandler extends LoggedObject {

    private Container container;

    public IceHandler(Container container) {
        this.container = container;
    }

    public void handle(MessageEntity message) {
        getLogger().debug("Hanling message " + message);
        container.getQueue().addMessage(message);
    }
}
