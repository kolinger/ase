package cz.uhk.fim.ase.communication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.Util;
import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.GlobalListener;
import cz.uhk.fim.ase.container.Container;

import java.net.URL;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class GlobalListenerImpl extends LoggedObject implements GlobalListener {

    private Container container;

    public void setContainer(Container container) {
        this.container = container;
    }

    public void listen(String address) {
        Communicator communicator = createCommunicator();

        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("DiscoverHello", "tcp");
        Ice.ObjectPrx hello = adapter.addWithUUID(new GlobalHelloMessage(container));
        adapter.activate();

        String[] parts = address.split(":");
        ObjectAdapter discoverAdapter = communicator.createObjectAdapterWithEndpoints("Discover",
                "udp -h " + parts[0] + " -p " + parts[1]);
        discoverAdapter.add(new GlobalHandler(hello), communicator.stringToIdentity("discover"));
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
