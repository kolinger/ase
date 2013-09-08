package cz.uhk.fim.virtualeconomy.container.agents.behaviours;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class TickerBehaviour extends Behavior {

    private Long period = 1000L;

    public TickerBehaviour() {
    }

    public TickerBehaviour(Long period) {
        this.period = period;
    }

    public Boolean isDone() {
        return false;
    }

    protected void setup() {
    }

    protected void action() {
        doTick();
        try {
            Thread.sleep(period);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    abstract protected void doTick();
}
