package cz.uhk.fim.ase.reporting.model;

import java.util.Map;
import java.util.UUID;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface Model {

    void save(Map<UUID, ? extends ReportAgent> agents);
}
