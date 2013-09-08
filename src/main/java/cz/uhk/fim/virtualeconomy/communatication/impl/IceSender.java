package cz.uhk.fim.virtualeconomy.communatication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectPrx;
import Ice.Util;
import cz.uhk.fim.virtualeconomy.common.LoggedObject;
import cz.uhk.fim.virtualeconomy.communatication.Sender;
import cz.uhk.fim.virtualeconomy.model.ContainerEntity;
import cz.uhk.fim.virtualeconomy.model.MessageEntity;
import slices.Message;
import slices.MessageTransporterPrx;
import slices.MessageTransporterPrxHelper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class IceSender extends LoggedObject implements Sender {

    private Map<String, ObjectPrx> proxies = new HashMap<String, ObjectPrx>();
    private Communicator communicator;

    public IceSender() {
        communicator = createCommunicator();
    }

    @Override
    public void send(MessageEntity message) {
        getLogger().debug("Sending message {}", message);

        Message data = Converter.convertMessage(message);
        for (MessageEntity.Address address : message.getReceivers()) {
            ObjectPrx proxy = getProxy(address.getContainer());
            MessageTransporterPrx transporter = MessageTransporterPrxHelper.checkedCast(proxy);
            transporter.transport(data);
        }
    }

    private synchronized ObjectPrx getProxy(ContainerEntity container) {
        String key = container.getAddress();
        if (proxies.containsKey(key)) {
            getLogger().debug("Getting proxy {} from cache", key);
            return proxies.get(key);
        }
        getLogger().debug("Creating proxy {}", key);
        String string = "handler:tcp -h " + container.getHost() + " -p " + container.getPort();
        ObjectPrx proxy = communicator.stringToProxy(string);
        proxies.put(key, proxy);
        return proxy;
    }

    private Communicator createCommunicator() {
        InitializationData initializationData = new InitializationData();
        initializationData.properties = Ice.Util.createProperties();
        URL file = this.getClass().getClassLoader().getResource("config.client");
        if (file != null) {
            initializationData.properties.load(file.getFile());
        }
        return Util.initialize(initializationData);
    }
}
