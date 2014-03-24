package cz.uhk.fim.ase.communication.impl;

import Ice.Current;
import cz.uhk.fim.ase.communication.impl.internal._GlobalSyncMessageDisp;
import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.TickManager;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class GlobalSyncMessage extends _GlobalSyncMessageDisp {

    @Override
    public long getTick(Current __current) {
        return TickManager.get().getCurrentTick();
    }

    @Override
    public String getId(Current __current) {
        return Config.get().system.id;
    }
}
