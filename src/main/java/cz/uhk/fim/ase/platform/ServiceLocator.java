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
import cz.uhk.fim.ase.reporting.model.Model;
import cz.uhk.fim.ase.reporting.model.PostgreSqlModel;

/**
 * ServiceLocator holding all services on platform.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ServiceLocator {

    // services
    private ConfigurationLoader configurationLoader = new XmlLoader();
    private Configuration configuration = configurationLoader.loadConfiguration();

    private Model model = new PostgreSqlModel();
    private ReportService reportService = new ReportService(model);

    private MessagesQueue messagesQueue = new MessagesQueueImpl();
    private Sender sender = new SenderImpl();
    private Listener listener = new ListenerImpl();
    private Subscriber subscriber = new SubscriberImpl();
    private Broadcaster broadcaster = new BroadcasterImpl();

    private SyncService syncService = new SyncService();

    // internal
    private static ServiceLocator instance;

    /**
     * ***************************** accessors *****************************
     */

    public static Configuration getConfig() {
        return getInstance().configuration;
    }

    public static ReportService getReportService() {
        return getInstance().reportService;
    }

    public static MessagesQueue getMessagesQueue() {
        return getInstance().messagesQueue;
    }

    public static Sender getSender() {
        return getInstance().sender;
    }

    public static Listener getListener() {
        return getInstance().listener;
    }

    public static Subscriber getSubscriber() {
        return getInstance().subscriber;
    }

    public static Broadcaster getBroadcaster() {
        return getInstance().broadcaster;
    }

    public static SyncService getSyncService() {
        return getInstance().syncService;
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
