package cz.uhk.fim.ase.communication.impl;

import Ice.Current;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.impl.internal._MessagesHandlerDisp;
import cz.uhk.fim.ase.model.impl.MessageEntityImpl;
import org.slf4j.LoggerFactory;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesHandler extends _MessagesHandlerDisp {

    public MessagesQueue queue;

    public MessagesHandler(MessagesQueue queue) {
        this.queue = queue;
    }

    public void handle(cz.uhk.fim.ase.model.internal.MessageEntity message, Current current) {
        LoggerFactory.getLogger(getClass()).debug("Transporting message {}", message);
        queue.addMessage(new MessageEntityImpl(message));
    }
}
