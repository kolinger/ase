package cz.uhk.fim.ase.communication.impl;

import org.zeromq.ZMQ;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ContextHolder {

    private static ZMQ.Context context;

    public static ZMQ.Context getContext() {
        if (context == null) {
            context = ZMQ.context(10); // TODO: add thread count to config
        }
        return context;
    }
}
