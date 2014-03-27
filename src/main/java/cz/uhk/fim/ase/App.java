package cz.uhk.fim.ase;

import cz.uhk.fim.ase.platform.Platform;
import cz.uhk.fim.ase.platform.StaticPlatform;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Main class witch runs application.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class App {

    public static void main(String[] args) {
        initialization();
        run();
    }

    private static void run() {
        Platform platform = new StaticPlatform();
        platform.run();
    }

    private static void initialization() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
