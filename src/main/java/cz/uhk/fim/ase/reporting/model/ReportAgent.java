package cz.uhk.fim.ase.reporting.model;

import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface ReportAgent {

    String getReportId();

    Map<String, String> getReportValues();
}
