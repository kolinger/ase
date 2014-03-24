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

        // main adapter
        String[] parts = address.split(":");
        ObjectAdapter discoverAdapter = communicator.createObjectAdapterWithEndpoints("Global",
                "udp -h " + parts[0] + " -p " + parts[1]);

        // hello adapter
        ObjectAdapter helloAdapter = communicator.createObjectAdapterWithEndpoints("HelloResponse", "tcp");
        Ice.ObjectPrx helloResponse = helloAdapter.addWithUUID(new GlobalHelloMessage(container));
        helloAdapter.activate();
        discoverAdapter.add(new GlobalHandler(helloResponse), communicator.stringToIdentity("hello"));

        // sync adapter
        ObjectAdapter syncAdapter = communicator.createObjectAdapterWithEndpoints("SyncResponse", "tcp");
        Ice.ObjectPrx syncResponse = syncAdapter.addWithUUID(new GlobalSyncMessage());
        syncAdapter.activate();
        discoverAdapter.add(new GlobalHandler(syncResponse), communicator.stringToIdentity("sync"));

        // run
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
