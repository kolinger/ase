package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
public class System {

    public Integer computeThreads = 100;
    public Integer communicationThreads = 10;

    public String subscriberAddress = "localhost:10000";
    public String broadcasterAddress = "localhost:20000";
    public String listenerAddress = "localhost:30000";
}
