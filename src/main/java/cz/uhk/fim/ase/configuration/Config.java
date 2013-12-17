package cz.uhk.fim.ase.configuration;

import cz.uhk.fim.ase.configuration.model.Configuration;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.URL;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class Config {

    private static Configuration instance;

    public static Configuration get() {
        if (instance == null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                URL url = ClassLoader.getSystemResource("configuration.xml");
                instance = (Configuration) unmarshaller.unmarshal(url);
            } catch (JAXBException e) {
                LoggerFactory.getLogger(Config.class).error("Failed configuration loading", e);
            }
        }
        return instance;
    }
}
