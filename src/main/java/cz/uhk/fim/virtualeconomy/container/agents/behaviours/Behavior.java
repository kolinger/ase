package cz.uhk.fim.virtualeconomy.container.agents.behaviours;

import cz.uhk.fim.virtualeconomy.common.LoggedObject;

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
