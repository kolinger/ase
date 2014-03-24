package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlRootElement(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
public class Configuration {

    public String instance;

    public Environment environment = new Environment();
    public System system = new System();
    public ReportDatabase reportDatabase = new ReportDatabase();
}
