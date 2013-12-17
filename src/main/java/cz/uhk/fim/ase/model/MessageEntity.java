package cz.uhk.fim.ase.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessageEntity {

    private Address sender;
    private List<Address> receivers = new ArrayList<Address>();
    private Type type;
    private String content;

    public Address getSender() {
        return sender;
    }

    public void setSender(UUID agentId, ContainerEntity container) {
        sender = new Address(agentId, container);
    }

    public List<Address> getReceivers() {
        return receivers;
    }

    public void addReceiver(UUID agentId, ContainerEntity container) {
        Address receiver = new Address(agentId, container);
        receivers.add(receiver);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "sender=" + sender +
                ", receivers=" + receivers +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }

    public class Address {

        private UUID agentId;
        private ContainerEntity container;

        public Address(UUID agentId, ContainerEntity container) {
            this.agentId = agentId;
            this.container = container;
        }

        public UUID getAgentId() {
            return agentId;
        }

        public void setAgentId(UUID agentId) {
            this.agentId = agentId;
        }

        public ContainerEntity getContainer() {
            return container;
        }

        public void setContainer(ContainerEntity container) {
            this.container = container;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "agentId=" + agentId +
                    ", container=" + container +
                    '}';
        }
    }

    /**
     * See http://jmvidal.cse.sc.edu/talks/agentcommunication/performatives.html
     */
    public enum Type {
        ACCEPT_PROPOSAL,
        AGREE,
        CANCEL,
        CALL_FOR_PROPOSALS,
        CONFIRM,
        DISCONFIRM,
        FAILURE,
        INFORM,
        INFORM_IF,
        INFORM_REF,
        NOT_UNDERSTOOD,
        PROPAGATE,
        PROPOSE,
        PROXY,
        QUERY_IF,
        QUERY_REF,
        REFUSE,
        REJECT_PROPOSAL,
        REQUEST,
        REQUEST_WHEN,
        REQUEST_WHENEVER,
        SUBSCRIBE
    }
}
