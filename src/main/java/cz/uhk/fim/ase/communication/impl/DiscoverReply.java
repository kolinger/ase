package cz.uhk.fim.ase.communication.impl;

import slices._DiscoverReplyDisp;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class DiscoverReply extends _DiscoverReplyDisp {

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
