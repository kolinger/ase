package cz.uhk.fim.ase.platform;

import cz.uhk.fim.ase.communication.Broadcaster;
import cz.uhk.fim.ase.communication.Listener;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.communication.Subscriber;
import cz.uhk.fim.ase.communication.impl.BroadcasterImpl;
import cz.uhk.fim.ase.communication.impl.ListenerImpl;
import cz.uhk.fim.ase.communication.impl.MessagesQueueImpl;
import cz.uhk.fim.ase.communication.impl.SenderImpl;
import cz.uhk.fim.ase.communication.impl.SubscriberImpl;
import cz.uhk.fim.ase.configuration.ConfigurationLoader;
import cz.uhk.fim.ase.configuration.XmlLoader;
import cz.uhk.fim.ase.configuration.model.Configuration;
import cz.uhk.fim.ase.reporting.ReportService;
import cz.uhk.fim.ase.reporting.model.Mongo;

/**
 * ServiceLocator holding all services on platform.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ServiceLocator {

    // services
    private ConfigurationLoader configurationLoader = new XmlLoader();
    private Configuration configuration = configurationLoader.loadConfiguration();

    private ReportService reportService;

    private MessagesQueue messagesQueue;
    private Sender sender;
    private Listener listener;
    private Subscriber subscriber;
    private Broadcaster broadcaster;

    private Registry registry;
    private SyncService syncService;
    private Monitor monitor;

    // internal
    private static ServiceLocator instance;

    /**
     * ***************************** accessors *****************************
     */

    public static Configuration getConfig() {
        return getInstance().configuration;
    }

    public static ReportService getReportService() {
        if (getInstance().reportService == null) {
            getInstance().reportService = new ReportService(new Mongo());
        }
        return getInstance().reportService;
    }

    public static MessagesQueue getMessagesQueue() {
        if (getInstance().messagesQueue == null) {
            getInstance().messagesQueue = new MessagesQueueImpl();
        }
        return getInstance().messagesQueue;
    }

    public static Sender getSender() {
        if (getInstance().sender == null) {
            getInstance().sender = new SenderImpl();
        }
        return getInstance().sender;
    }

    public static Listener getListener() {
        if (getInstance().listener == null) {
            getInstance().listener = new ListenerImpl();
        }
        return getInstance().listener;
    }

    public static Subscriber getSubscriber() {
        if (getInstance().subscriber == null) {
            getInstance().subscriber = new SubscriberImpl();
        }
        return getInstance().subscriber;
    }

    public static Broadcaster getBroadcaster() {
        if (getInstance().broadcaster == null) {
            getInstance().broadcaster = new BroadcasterImpl();
        }
        return getInstance().broadcaster;
    }

    public static SyncService getSyncService() {
        if (getInstance().syncService == null) {
            getInstance().syncService = new SyncService();
        }
        return getInstance().syncService;
    }

    public static Monitor getMonitor() {
        if (getInstance().monitor == null) {
            getInstance().monitor = new Monitor();
        }
        return getInstance().monitor;
    }

    public static Registry getRegistry() {
        if (getInstance().registry == null) {
            getInstance().registry = new Registry();
        }
        return getInstance().registry;
    }

    /**
     * ***************************** helpers *****************************
     */

    private static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }
}
