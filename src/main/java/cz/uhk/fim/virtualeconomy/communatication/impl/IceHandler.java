package cz.uhk.fim.virtualeconomy.communatication.impl;

import cz.uhk.fim.virtualeconomy.common.LoggedObject;
import cz.uhk.fim.virtualeconomy.container.Container;
import cz.uhk.fim.virtualeconomy.model.MessageEntity;

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
