package cz.uhk.fim.ase.communication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.Util;
import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.container.Container;

import java.net.URL;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class DiscoverListener extends LoggedObject {

    private Container container;
    private Communicator communicator;

    public DiscoverListener(Container container) {
        this.container = container;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public void listen(String address, Integer port) {
        communicator = createCommunicator();

        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("DiscoverHello", "tcp");
        Ice.ObjectPrx hello = adapter.addWithUUID(new DiscoverHello(container));
        adapter.activate();

        ObjectAdapter discoverAdapter = communicator.createObjectAdapterWithEndpoints("Discover",
                "udp -h " + address +  " -p " + port);
        discoverAdapter.add(new DiscoverHandler(hello), communicator.stringToIdentity("discover"));
        discoverAdapter.activate();

        communicator.waitForShutdown();
        communicator.destroy();
    }

    private Communicator createCommunicator() {
        InitializationData initializationData = new InitializationData();
        initializationData.properties = Ice.Util.createProperties();
        URL file = this.getClass().getClassLoader().getResource("/config.server");
        if (file != null) {
            initializationData.properties.load(file.getFile());
        }
        return Util.initialize(initializationData);
    }
}
