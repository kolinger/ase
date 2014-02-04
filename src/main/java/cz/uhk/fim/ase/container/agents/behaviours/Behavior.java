package cz.uhk.fim.ase.container.agents.behaviours;

import cz.uhk.fim.ase.common.LoggedObject;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Behavior extends LoggedObject implements Runnable {

    public void run() {
        setup();
        action();
    }

    abstract public Boolean isDone();

    abstract protected void setup();

    abstract protected void action();
}
