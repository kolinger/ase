package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
public class System {

    // misc
    public Integer threadsPool = 100;

    // direct sender
    public String directSenderAddress = "localhost";
    public Integer directSenderPort = 10000;

    // global sender
    public String globalSenderAddress = "239.255.1.1";
    public Integer globalSenderPort = 10001;
}
