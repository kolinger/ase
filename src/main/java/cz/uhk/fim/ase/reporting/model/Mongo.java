package cz.uhk.fim.ase.reporting.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import cz.uhk.fim.ase.configuration.model.Configuration;
import cz.uhk.fim.ase.configuration.model.ReportDatabase;
import cz.uhk.fim.ase.platform.ServiceLocator;
import cz.uhk.fim.ase.platform.agents.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class Mongo implements Model {

    private Logger logger = LoggerFactory.getLogger(Mongo.class);
    private MongoClient client;
    private DBCollection collection;

    @Override
    public void save(Map<String, ? extends Agent> agents) {
        ReportDatabase config = ServiceLocator.getConfig().reportDatabase;

        if (client == null) {
            try {
                client = new MongoClient(config.address, config.port);

                DB db = client.getDB(config.database);
                String collectionName = config.collection;
                if (collectionName == null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                    collectionName = "reports-" + dateFormat.format(new Date());
                }
                collection = db.getCollection(collectionName);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                logger.error("Connection to mongo database failed", e);
                return;
            }
        }

        logger.debug("Report tick...");
        Long tick = ServiceLocator.getSyncService().getTick();

        for (Agent agent : agents.values()) {
            for (Map.Entry<String, String> entry : agent.getReportValues().entrySet()) {
                BasicDBObject document = new BasicDBObject();
                document.put("tick", tick);
                document.put("node", agent.getEntity().getNode());
                document.put("agent", agent.getEntity().getId());
                document.put("key", entry.getKey());
                document.put("value", entry.getValue());
                collection.insert(document);
            }
        }
    }
}
