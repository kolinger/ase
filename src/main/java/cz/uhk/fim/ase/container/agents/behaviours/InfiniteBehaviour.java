package cz.uhk.fim.ase.container.agents.behaviours;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class InfiniteBehaviour extends Behavior {

    public Boolean isDone() {
        return false;
    }

    protected void setup() {
        // virtual
    }

    protected void action() {
        doCycle();
    }

    abstract protected void doCycle();
}
