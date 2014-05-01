package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.communication.impl.SenderImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class SyncService {

    private Long tick = 1L;
    private Map<String, Long> nodesStatuses = new ConcurrentHashMap<>();

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
        ServiceLocator.getBroadcaster().sendSync();

        if (nodesStatuses.size() == 0) { // standalone mode
            tick++;
            return;
        }

        Boolean done;
        do {
            done = true;
            for (Long nodeTick : nodesStatuses.values()) {
                if (nodeTick.compareTo(tick) < 0) {
                    done = false;
                    break;
                }
            }
        } while (!done);
        tick++;
    }
}
