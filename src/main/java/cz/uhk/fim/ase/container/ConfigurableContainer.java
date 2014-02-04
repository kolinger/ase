package cz.uhk.fim.ase.container;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ConfigurableContainer extends Container {

    public ConfigurableContainer(String host, Integer port) {
        super(host, port);
    }

    @Override
    protected void setup() {
        getLogger().error("Configurable container is not implemented yet");
    }
}
