package cz.uhk.fim.ise.configuration.model;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
@XmlType(namespace = "http://fim.uhk.cz/ise/configuration.xsd")
public class Concurrency {

    public Integer threadsPool = 100;
}
