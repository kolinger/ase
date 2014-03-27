package cz.uhk.fim.ase.platform;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class SyncService {

    private Long tick = 1L;
    private Map<String, Long> nodesStatuses = new HashMap<String, Long>();

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
        Boolean done = true;
        do {

            for (Long tick : nodesStatuses.values()) {
                if (tick.compareTo(tick) < 0) {
                    done = false;
                    break;
                }
            }
        } while (!done);
        tick++;
    }
}
