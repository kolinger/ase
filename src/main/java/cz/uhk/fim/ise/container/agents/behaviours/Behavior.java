package cz.uhk.fim.ise.container.agents.behaviours;

import cz.uhk.fim.ise.common.LoggedObject;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Behavior extends LoggedObject {

    private Boolean blocked = false;

    public Boolean isRunnable() {
        return !blocked;
    }

    public void block() {
        blocked = true;
    }

    public void run() {
        setup();
        action();
    }

    abstract public Boolean isDone();

    abstract protected void setup();

    abstract protected void action();
}
