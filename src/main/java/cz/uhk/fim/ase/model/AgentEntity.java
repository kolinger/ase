package cz.uhk.fim.ase.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class AgentEntity {

    private UUID id;
    private ContainerEntity container;
    private CoordinatesEntity position;
    private Map<String, String> properties = new HashMap<String, String>();

    public AgentEntity(UUID id, ContainerEntity container) {
        this.id = id;
        this.container = container;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ContainerEntity getContainer() {
        return container;
    }

    public void setContainer(ContainerEntity container) {
        this.container = container;
    }

    public CoordinatesEntity getPosition() {
        if (position == null) {
            position = new CoordinatesEntity();
        }
        return position;
    }

    public void setPosition(CoordinatesEntity position) {
        this.position = position;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public class CoordinatesEntity {

        private Double latitude;
        private Double longitude;

        private Double getLatitude() {
            return latitude;
        }

        private void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        private Double getLongitude() {
            return longitude;
        }

        private void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "CoordinatesEntity{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AgentEntity{" +
                "id=" + id +
                ", container=" + container +
                ", position=" + position +
                ", properties=" + properties +
                '}';
    }
}
