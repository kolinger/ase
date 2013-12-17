package cz.uhk.fim.ise.communatication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.Util;
import cz.uhk.fim.ise.common.LoggedObject;
import cz.uhk.fim.ise.communatication.Listener;
import cz.uhk.fim.ise.communatication.MessagesQueue;
import cz.uhk.fim.ise.container.Container;
import cz.uhk.fim.ise.model.ContainerEntity;

import java.net.URL;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class IceListener extends LoggedObject implements Listener {

    private ContainerEntity address;
    private MessagesQueue queue;

    public IceListener(Container container) {
        this.address = container.getAddress();
        this.queue = container.getQueue();
    }

    public void listen() {
        Communicator communicator = createCommunicator();

        String endpoint = "tcp -h " + address.getHost() +  " -p " + address.getPort();
        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Handler", endpoint);
        adapter.add(new MessageTransporter(queue), communicator.stringToIdentity("handler"));
        adapter.activate();

        communicator.waitForShutdown();
        communicator.destroy();
    }

    private Communicator createCommunicator() {
        InitializationData initializationData = new InitializationData();
        initializationData.properties = Ice.Util.createProperties();
        URL file = this.getClass().getClassLoader().getResource("config.server");
        if (file != null) {
            initializationData.properties.load(file.getFile());
        }
        return Util.initialize(initializationData);
    }
}
