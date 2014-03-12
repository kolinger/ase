package cz.uhk.fim.ase.communication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectPrx;
import Ice.Util;
import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.MessagesSender;
import cz.uhk.fim.ase.communication.impl.internal.MessagesHandlerPrx;
import cz.uhk.fim.ase.communication.impl.internal.MessagesHandlerPrxHelper;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.impl.MessageEntityImpl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesSenderImpl extends LoggedObject implements MessagesSender {

    private Map<String, ObjectPrx> proxies = new HashMap<String, ObjectPrx>();
    private Communicator communicator;

    public MessagesSenderImpl() {
        communicator = createCommunicator();
    }

    @Override
    public void send(MessageEntity message) {
        getLogger().debug("Sending message {}", message);

        for (AgentEntity receiver : message.getReceivers()) {
            ObjectPrx proxy = getProxy(receiver.getContainer());
            MessagesHandlerPrx transporter = MessagesHandlerPrxHelper.checkedCast(proxy);
            transporter.handle(MessageEntityImpl.convert(message));
        }
    }

    private synchronized ObjectPrx getProxy(String container) {
        if (proxies.containsKey(container)) {
            getLogger().debug("Getting proxy {} from cache", container);
            return proxies.get(container);
        }
        getLogger().debug("Creating proxy {}", container);
        String[] parts = container.split(":");
        String string = "handler:tcp -h " + parts[0] + " -p " + parts[1];
        ObjectPrx proxy = communicator.stringToProxy(string);
        proxies.put(container, proxy);
        return proxy;
    }

    private Communicator createCommunicator() {
        InitializationData initializationData = new InitializationData();
        initializationData.properties = Ice.Util.createProperties();
        URL file = this.getClass().getClassLoader().getResource("/config.client");
        if (file != null) {
            initializationData.properties.load(file.getFile());
        }
        return Util.initialize(initializationData);
    }
}
