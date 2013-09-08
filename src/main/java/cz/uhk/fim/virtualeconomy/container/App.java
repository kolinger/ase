package cz.uhk.fim.virtualeconomy.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);
    private static Properties configuration;

    public static void main(String[] args) {
        initialization();
        loadConfiguration();
        run("localhost", 10000);
    }

    public static Properties getConfiguration() {
        return configuration;
    }

    private static void run(String host, Integer port) {
        logger.debug("Creating container on {}:{}", host, port);
        Container container = new TestContainer(host, port);
        container.run();
    }

    private static void loadConfiguration() {
        InputStream file = App.class.getClassLoader().getResourceAsStream("configuration.properties");
        configuration = createDefaultConfiguration();
        try {
            configuration.load(file);
        } catch (IOException e) {
            logger.info("Cannot find configuration.properties in classpath");
        }
    }

    private static Properties createDefaultConfiguration() {
        Properties configuration = new Properties();
        configuration.setProperty("concurrency.threads_pool", "100");
        configuration.setProperty("concurrency.scheduler_pool", "100");
        return configuration;
    }

    private static void initialization() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
