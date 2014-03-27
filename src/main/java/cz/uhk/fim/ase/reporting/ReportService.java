package cz.uhk.fim.ase.reporting;

import cz.uhk.fim.ase.platform.agents.Agent;
import cz.uhk.fim.ase.reporting.model.Model;

import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ReportService {

    private Model model;

    public ReportService(Model model) {
        this.model = model;
    }

    public void doReport(Map<String, Agent> agents) {
        model.save(agents);
    }
}
