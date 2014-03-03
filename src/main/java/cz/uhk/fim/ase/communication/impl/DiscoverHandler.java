package cz.uhk.fim.ase.communication.impl;

import Ice.Current;
import slices.DiscoverReplyPrx;
import slices._DiscoverDisp;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class DiscoverHandler extends _DiscoverDisp {

    private Ice.ObjectPrx object;

    public DiscoverHandler(Ice.ObjectPrx object)
    {
        this.object = object;
    }

    @Override
    public void lookup(DiscoverReplyPrx reply, Current __current) {
        reply.reply(object);
    }
}
