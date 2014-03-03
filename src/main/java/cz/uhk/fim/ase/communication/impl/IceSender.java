package cz.uhk.fim.ase.communication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectPrx;
import Ice.Util;
import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.Sender;
import slices.AgentEntity;
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
    public void send(Message message) {
        getLogger().debug("Sending message {}", message);

        for (AgentEntity receiver : message.receivers) {
            ObjectPrx proxy = getProxy(receiver.container);
            MessageTransporterPrx transporter = MessageTransporterPrxHelper.checkedCast(proxy);
            transporter.transport(message);
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
