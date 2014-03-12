package cz.uhk.fim.ase.communication.impl;

import Ice.Current;
import cz.uhk.fim.ase.communication.impl.internal.GlobalReplyPrx;
import cz.uhk.fim.ase.communication.impl.internal._GlobalHandlerDisp;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class GlobalHandler extends _GlobalHandlerDisp {

    private Ice.ObjectPrx object;

    public GlobalHandler(Ice.ObjectPrx object) {
        this.object = object;
    }

    @Override
    public void lookup(GlobalReplyPrx reply, Current __current) {
        reply.reply(object);
    }
}