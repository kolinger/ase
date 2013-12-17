package cz.uhk.fim.ise.container.agents.behaviours;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class CyclicBehaviour extends Behavior {

    public Boolean isDone() {
        return false;
    }

    protected void setup() {
    }

    protected void action() {
        doCycle();
    }

    abstract protected void doCycle();
}
