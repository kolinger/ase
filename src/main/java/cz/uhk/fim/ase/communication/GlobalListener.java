package cz.uhk.fim.ase.communication;

import cz.uhk.fim.ase.container.Container;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface GlobalListener {

    void setContainer(Container container);

    void listen(String address);
}
