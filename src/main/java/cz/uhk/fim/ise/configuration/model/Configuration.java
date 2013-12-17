package cz.uhk.fim.ise.configuration.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlRootElement(namespace = "http://fim.uhk.cz/ise/configuration.xsd")
@XmlType(namespace = "http://fim.uhk.cz/ise/configuration.xsd")
public class Configuration {

    public Concurrency concurrency;
}
