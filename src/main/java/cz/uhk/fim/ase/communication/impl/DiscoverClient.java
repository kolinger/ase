package cz.uhk.fim.ase.communication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Util;
import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.container.Registry;
import slices.AgentEntity;
import slices.DiscoverHelloPrx;
import slices.DiscoverHelloPrxHelper;
import slices.DiscoverPrx;
import slices.DiscoverPrxHelper;
import slices.DiscoverReplyPrx;
import slices.DiscoverReplyPrxHelper;

import java.net.URL;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class DiscoverClient extends LoggedObject {

    public String process(String address, Integer port) {
        Communicator communicator = createCommunicator();

        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("DiscoverReply", "tcp");
        DiscoverReply reply = new DiscoverReply();
        DiscoverReplyPrx replyProxy = DiscoverReplyPrxHelper.uncheckedCast(adapter.addWithUUID(reply));
        adapter.activate();

        ObjectPrx proxy = communicator.stringToProxy("discover:udp -h " + address + " -p " + port);
        DiscoverPrx discover = DiscoverPrxHelper.uncheckedCast(proxy.ice_datagram());
        discover.lookup(replyProxy);
        Ice.ObjectPrx base = reply.waitReply(2000); // TODO: add timeout to config

        // no replies
        if (base == null) {
            return null;
        }

        DiscoverHelloPrx hello = DiscoverHelloPrxHelper.checkedCast(base);
        if (hello == null) { // invalid reply
            return null;
        }

        for (AgentEntity agentEntity : hello.getAgents()) {
            Registry.get().register(agentEntity);
        }

        return hello.getInstanceId();
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
