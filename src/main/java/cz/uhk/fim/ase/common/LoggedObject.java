package cz.uhk.fim.ase.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class LoggedObject {

    public Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

}
