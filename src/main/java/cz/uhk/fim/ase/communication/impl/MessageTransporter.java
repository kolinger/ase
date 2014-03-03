package cz.uhk.fim.ase.communication.impl;

import Ice.Current;
import cz.uhk.fim.ase.communication.MessagesQueue;
import org.slf4j.LoggerFactory;
import slices.Message;
import slices._MessageTransporterDisp;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessageTransporter extends _MessageTransporterDisp {

    public MessagesQueue queue;

    public MessageTransporter(MessagesQueue queue) {
        this.queue = queue;
    }

    public void transport(Message message, Current current) {
        LoggerFactory.getLogger(getClass()).debug("Transporting message {}", message);
        queue.addMessage(message);
    }
}
