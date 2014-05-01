package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd", propOrder = {})
public class Environment {

    // misc
    public Integer agentsCount = 0;
    public Boolean monitorAgent = true;

    // ticks
    public Long finalTick;
    public Long reportEveryTick;
}
