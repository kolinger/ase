package cz.uhk.fim.ase.container.agents.behaviours;

import cz.uhk.fim.ase.common.LoggedObject;

import java.util.concurrent.Callable;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Behavior extends LoggedObject implements Callable<Object> {

    public Object call() {
        setup();
        action();
        return null;
    }

    abstract public Boolean isDone();

    abstract protected void setup();

    abstract protected void action();
}
