package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.communication.impl.internal._GlobalReplyDisp;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class GlobalReply extends _GlobalReplyDisp {

    private Ice.ObjectPrx object;

    @Override
    public synchronized void reply(Ice.ObjectPrx object, Ice.Current current) {
        if (this.object == null) {
            this.object = object;
        }
        notify();
    }

    public synchronized Ice.ObjectPrx waitReply(long timeout) {
        long end = System.currentTimeMillis() + timeout;
        while (object == null) {
            long delay = end - System.currentTimeMillis();
            if (delay > 0) {
                try {
                    wait(delay);
                } catch (java.lang.InterruptedException ex) {
                    // skip
                }
            } else {
                break;
            }
        }
        return object;
    }
}
