package cz.uhk.fim.ise.communatication.impl;

import cz.uhk.fim.ise.common.LoggedObject;
import cz.uhk.fim.ise.container.Container;
import cz.uhk.fim.ise.model.MessageEntity;

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
