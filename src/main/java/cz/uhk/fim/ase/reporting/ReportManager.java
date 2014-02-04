package cz.uhk.fim.ase.reporting;

import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.reporting.model.Model;
import cz.uhk.fim.ase.reporting.model.PostgreSqlModel;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ReportManager {

    private Container container;
    private Model model = new PostgreSqlModel();

    public ReportManager(Container container) {
        this.container = container;
    }

    public void doReport() {
        model.save(container.getAgents());
    }
}
