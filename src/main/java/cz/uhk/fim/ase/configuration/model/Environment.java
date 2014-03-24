package cz.uhk.fim.ase.configuration.model;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlType(namespace = "http://fim.uhk.cz/ase/configuration.xsd")
public class Environment {

    // misc
    public Integer agentsCount = 0;

    // ticks
    public Long finalTick;
    public Long reportEveryTick;
}
