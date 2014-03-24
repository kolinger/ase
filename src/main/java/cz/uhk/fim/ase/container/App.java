package cz.uhk.fim.ase.container;

import cz.uhk.fim.ase.configuration.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        initialization();
        run(Config.get().system.directSenderAddress, Config.get().system.directSenderPort);
    }

    private static void run(String host, Integer port) {
        logger.debug("Creating container on {}:{}", host, port);
        Container container = new StaticContainer(host, port);
        container.run();
    }

    private static void initialization() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
