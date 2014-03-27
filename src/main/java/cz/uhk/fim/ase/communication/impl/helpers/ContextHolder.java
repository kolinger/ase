package cz.uhk.fim.ase.communication.impl.helpers;

import cz.uhk.fim.ase.platform.ServiceLocator;
import org.zeromq.ZMQ;

/**
 * ContextHolder is internal helper for ZeroMQ communication layer and just holds communication threads.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ContextHolder {

    private static ZMQ.Context context;

    public static ZMQ.Context getContext() {
        if (context == null) {
            context = ZMQ.context(ServiceLocator.getConfig().system.communicationThreads);
        }
        return context;
    }
}
