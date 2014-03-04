package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
public class Container {

    public Long finalTick;

    public Long reportEveryTick = 5L;

    public Integer agentsCount = 1000000;

    public String address = "localhost";

    public Integer port = 10000;

    public String discoverAddress = "239.255.1.1";

    public Integer discoverPort = 10001;
}
