package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlRootElement(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd", propOrder = {})
public class Configuration {

    public Environment environment = new Environment();
    public System system = new System();
    public ReportDatabase reportDatabase = new ReportDatabase();
}
