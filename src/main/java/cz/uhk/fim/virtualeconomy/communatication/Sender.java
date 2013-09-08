package cz.uhk.fim.virtualeconomy.communatication;

import cz.uhk.fim.virtualeconomy.model.MessageEntity;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Sender {

    void send(MessageEntity message);
}
