package cz.uhk.fim.ase.configuration;

import cz.uhk.fim.ase.configuration.model.Configuration;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface ConfigurationLoader {

    Configuration loadConfiguration();
}
