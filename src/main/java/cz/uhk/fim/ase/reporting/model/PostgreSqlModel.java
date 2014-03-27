package cz.uhk.fim.ase.reporting.model;

import cz.uhk.fim.ase.platform.ServiceLocator;
import cz.uhk.fim.ase.platform.agents.Agent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class PostgreSqlModel implements Model {

    private Connection db;

    public PostgreSqlModel() {
        try {
            Class.forName("org.postgresql.Driver");
            db = createConnection();
            db.setAutoCommit(false);
        } catch (SQLException e) {
//            getLogger().error("Can't connect to database", e);
        } catch (ClassNotFoundException e) {
//            getLogger().error("Postgresql driver not found", e);
        }
    }

    @Override
    public void save(Map<String, ? extends Agent> agents) {
//        try {
//            for (Agent agent : agents.values()) {
//                for (Map.Entry<String, String> entry : agent.getReportValues().entrySet()) {
//                    String key = entry.getKey();
//                    String value = entry.getValue();
//
//                    try {
//                        PreparedStatement query = db.prepareStatement("INSERT INTO reports (instance, tick, agent, key, value) VALUES (?, ?, ?, ?, ?)");
//                        query.setString(1, ServiceLocator.getConfig().instance);
//                        query.setLong(2, TickManager.get().getCurrentTick());
//                        query.setString(3, agent.getEntity().getId());
//                        query.setString(4, key);
//                        query.setString(5, value);
//                        query.execute();
//                    } catch (SQLException e) {
//                        getLogger().error("Unable to create statement for agent states", e);
//                        db.rollback();
//                    }
//                }
//            }
//
//            db.commit();
//        } catch (SQLException e) {
//            getLogger().error("Can't save agents states to database", e);
//        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(ServiceLocator.getConfig().reportDatabase.jdbc, ServiceLocator.getConfig().reportDatabase.user,
                ServiceLocator.getConfig().reportDatabase.password);
    }
}
