package cz.uhk.fim.ase.reporting.model;

import cz.uhk.fim.ase.common.LoggedObject;
import cz.uhk.fim.ase.configuration.Config;
import cz.uhk.fim.ase.container.TickManager;
import cz.uhk.fim.ase.container.agents.Agent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class PostgreSqlModel extends LoggedObject implements Model {

    private Connection db;

    public PostgreSqlModel() {
        try {
            Class.forName("org.postgresql.Driver");
            db = createConnection();
            db.setAutoCommit(false);
        } catch (SQLException e) {
            getLogger().error("Can't connect to database", e);
        } catch (ClassNotFoundException e) {
            getLogger().error("Postgresql driver not found", e);
        }
    }

    @Override
    public void save(Map<String, ? extends Agent> agents) {
        try {
            for (Agent agent : agents.values()) {
                for (Map.Entry<String, String> entry : agent.getReportValues().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    try {
                        PreparedStatement query = db.prepareStatement("INSERT INTO reports (instance, tick, agent, key, value) VALUES (?, ?, ?, ?, ?)");
                        query.setString(1, Config.get().instance);
                        query.setLong(2, TickManager.get().getCurrentTick());
                        query.setString(3, agent.getEntity().id);
                        query.setString(4, key);
                        query.setString(5, value);
                        query.execute();
                    } catch (SQLException e) {
                        getLogger().error("Unable to create statement for agent states", e);
                        db.rollback();
                    }
                }
            }

            db.commit();
        } catch (SQLException e) {
            getLogger().error("Can't save agents states to database", e);
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(Config.get().reportDatabase.jdbc, Config.get().reportDatabase.user,
                Config.get().reportDatabase.password);
    }
}
