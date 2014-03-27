package cz.uhk.fim.ase.configuration;

import cz.uhk.fim.ase.configuration.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.URL;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class XmlLoader implements ConfigurationLoader {

    private Logger logger = LoggerFactory.getLogger(XmlLoader.class);

    @Override
    public Configuration loadConfiguration() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            URL url = this.getClass().getClassLoader().getResource("configuration.xml");
            return (Configuration) unmarshaller.unmarshal(url);
        } catch (JAXBException e) {
            logger.error("Configuration loading failed", e);
            return null;
        }
    }
}
