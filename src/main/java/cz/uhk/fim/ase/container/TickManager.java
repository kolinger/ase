package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.configuration.Config;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class TickManager {

    private static TickManager instance;
    private Long tick = 1L;
    private Long finalTick;
    private Long reportTick;
    private boolean readyForNextTick = false;

    public static TickManager get() {
        if (instance == null) {
            instance = new TickManager();
            instance.setFinalTick(Config.get().container.finalTick);
            instance.setReportTick(Config.get().container.reportEveryTick);
        }
        return instance;
    }

    public Long getCurrentTick() {
        return tick;
    }

    public boolean isReportTick() {
        return (tick % reportTick) == 0;
    }

    public void setReportTick(Long tick) {
        reportTick = tick;
    }

    public boolean isFinalTick() {
        return finalTick != null && finalTick != 0 && finalTick.equals(tick);
    }

    public void setFinalTick(Long tick) {
        finalTick = tick;
    }

    public boolean isReadyForNextTick() {
        return readyForNextTick;
    }

    public void setReadyState(boolean ready) {
        tick++;
        readyForNextTick = ready;
    }

    public void setReadyState() {
        setReadyState(true);
    }
}
