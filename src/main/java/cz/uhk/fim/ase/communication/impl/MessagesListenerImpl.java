package cz.uhk.fim.ase.communication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.Util;
import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.MessagesListener;
import cz.uhk.fim.ase.communication.MessagesQueue;

import java.net.URL;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesListenerImpl extends LoggedObject implements MessagesListener {

    private MessagesQueue queue;

    public void setQueue(MessagesQueue queue) {
        this.queue = queue;
    }

    public void listen(String address) {
        Communicator communicator = createCommunicator();

        String[] parts = address.split(":");
        String endpoint = "tcp -h " + parts[0] + " -p " + parts[1];
        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Handler", endpoint);
        adapter.add(new MessagesHandler(queue), communicator.stringToIdentity("handler"));
        adapter.activate();

        communicator.waitForShutdown();
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
