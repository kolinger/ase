package cz.uhk.fim.ase.communication;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface GlobalSender {

    void setAddress(String address);

    String sendHello();

    Long sendSync();
}
