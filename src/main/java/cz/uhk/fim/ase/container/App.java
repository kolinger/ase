package cz.uhk.fim.ase.container;

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
        run("localhost", 10000);
    }

    private static void run(String host, Integer port) {
        logger.debug("Creating container on {}:{}", host, port);
        Container container = new TestContainer(host, port);
        container.run();
    }

    private static void initialization() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
