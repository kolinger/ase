package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.communication.impl.SenderImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class SyncService {

    private Long tick = 1L;
    private Map<String, Long> nodesStatuses = new HashMap<String, Long>();
    private SenderImpl sender = (SenderImpl) ServiceLocator.getSender();

    public Long getTick() {
        return tick;
    }

    public void updateNodeState(String node, Long tick) {
        nodesStatuses.put(node, tick);
    }

    public Boolean isReportTick() {
        return (tick % ServiceLocator.getConfig().environment.reportEveryTick) == 0;
    }

    public Boolean isFinalTick() {
        return ServiceLocator.getConfig().environment.finalTick.equals(tick);
    }

    public void waitForOthers() {
        while (sender.getQueue().size() > 0) {
            // wait
        }
        ServiceLocator.getBroadcaster().sendSync();

        if (nodesStatuses.size() == 0) { // standalone mode
            tick++;
            return;
        }

        Boolean done;
        do {
            done = true;
            for (Long nodeTick : nodesStatuses.values()) {
                if (nodeTick.compareTo(tick) <= 0) {
                    done = false;
                    break;
                }
            }
        } while (!done);
        tick++;
    }
}
