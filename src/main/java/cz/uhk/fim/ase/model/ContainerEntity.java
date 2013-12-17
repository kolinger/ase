package cz.uhk.fim.ase.model;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ContainerEntity {

    private String host;
    private Integer port;

    public ContainerEntity(String address) throws InvalidAddressException {
        setAddress(address);
    }

    public ContainerEntity(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getAddress() {
        return host + ":" + port;
    }

    public void setAddress(String address) throws InvalidAddressException {
        String[] parts = address.split(":");
        if (parts.length != 2) {
            throw new InvalidAddressException();
        }
        host = parts[0];
        port = Integer.parseInt(parts[1]);
    }

    @Override
    public String toString() {
        return "ContainerEntity{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public class InvalidAddressException extends Exception {
    }
}