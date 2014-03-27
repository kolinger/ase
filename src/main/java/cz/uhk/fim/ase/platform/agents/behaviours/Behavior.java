package cz.uhk.fim.ase.platform.agents.behaviours;

import java.util.concurrent.Callable;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
abstract public class Behavior implements Callable<Object> {

    public Object call() {
        setup();
        action();
        return null;
    }

    abstract public Boolean isDone();

    abstract protected void setup();

    abstract protected void action();
}
