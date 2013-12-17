package cz.uhk.fim.ise.communatication;

import cz.uhk.fim.ise.model.MessageEntity;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Sender {

    void send(MessageEntity message);
}
