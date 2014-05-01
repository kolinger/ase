package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd", propOrder = {})
public class ReportDatabase {

    // mongodb connection details
    public String address;
    public Integer port;
    public String database = "ase";
    public String collection = null;
}
