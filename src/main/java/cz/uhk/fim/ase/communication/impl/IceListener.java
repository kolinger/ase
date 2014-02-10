package cz.uhk.fim.ase.communication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.Util;
import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.Listener;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.model.ContainerEntity;

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
