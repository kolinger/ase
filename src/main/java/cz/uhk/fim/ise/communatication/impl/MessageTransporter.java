package cz.uhk.fim.ise.communatication.impl;

import Ice.Current;
import cz.uhk.fim.ise.communatication.MessagesQueue;
import cz.uhk.fim.ise.model.MessageEntity;
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
        MessageEntity convertedMessage = Converter.convertMessage(message);
        LoggerFactory.getLogger(getClass()).debug("Transporting message {}", convertedMessage);
        queue.addMessage(convertedMessage);
    }
}
