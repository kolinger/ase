package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
public class System {

    public Integer computeThreads = 100;
    public Integer communicationThreads = 10;

    public String subscriberAddress = "127.0.0.1:10000";
    public String broadcasterAddress = "127.0.0.1:20000";
    public String listenerAddress = "0.0.0.0:30000";
    public String myAddress = "127.0.0.1:30000";
}
