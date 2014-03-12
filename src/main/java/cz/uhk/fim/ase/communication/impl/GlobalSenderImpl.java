package cz.uhk.fim.ase.communication.impl;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Util;
import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.communication.GlobalSender;
import cz.uhk.fim.ase.communication.impl.internal.GlobalHandlerPrx;
import cz.uhk.fim.ase.communication.impl.internal.GlobalHandlerPrxHelper;
import cz.uhk.fim.ase.communication.impl.internal.GlobalHelloMessagePrx;
import cz.uhk.fim.ase.communication.impl.internal.GlobalHelloMessagePrxHelper;
import cz.uhk.fim.ase.communication.impl.internal.GlobalReplyPrx;
import cz.uhk.fim.ase.communication.impl.internal.GlobalReplyPrxHelper;
import cz.uhk.fim.ase.container.Registry;
import cz.uhk.fim.ase.model.impl.AgentEntityImpl;

import java.net.URL;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class GlobalSenderImpl extends LoggedObject implements GlobalSender {

    private String host;
    private String port;

    public void setAddress(String address) {
        String[] parts = address.split(":");
        host = parts[0];
        port = parts[1];
    }

    public String sendHello() {
        Communicator communicator = createCommunicator();

        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("GlobalReply", "tcp");
        GlobalReply reply = new GlobalReply();
        GlobalReplyPrx replyProxy = GlobalReplyPrxHelper.uncheckedCast(adapter.addWithUUID(reply));
        adapter.activate();


        ObjectPrx proxy = communicator.stringToProxy("discover:udp -h " + host + " -p " + port);
        GlobalHandlerPrx discover = GlobalHandlerPrxHelper.uncheckedCast(proxy.ice_datagram());
        discover.lookup(replyProxy);
        Ice.ObjectPrx base = reply.waitReply(2000); // TODO: add timeout to config

        // no replies
        if (base == null) {
            return null;
        }

        GlobalHelloMessagePrx hello = GlobalHelloMessagePrxHelper.checkedCast(base);
        if (hello == null) { // invalid reply
            return null;
        }

        getLogger().info("Discovered instance ID {} and {} foreign agent(s)", hello.getInstanceId(), hello.getAgents().length);

        for (cz.uhk.fim.ase.model.internal.AgentEntity agentEntity : hello.getAgents()) {
            Registry.get().register(new AgentEntityImpl(agentEntity));
        }

        return hello.getInstanceId();
    }

    private

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
