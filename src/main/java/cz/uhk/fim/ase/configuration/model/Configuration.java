package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlRootElement(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
public class Configuration {

    public Concurrency concurrency = new Concurrency();

    public ReportDatabase reportDatabase = new ReportDatabase();

    public String instance;

    public Container container = new Container();
}
