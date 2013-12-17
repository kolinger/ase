package cz.uhk.fim.ase.communatication.impl;

import cz.uhk.fim.ase.model.ContainerEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import slices.ContainerAddress;
import slices.MessageAddress;

import java.util.UUID;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class Converter {

    public static MessageEntity convertMessage(slices.Message data) {
        MessageEntity message = new MessageEntity();
        message.setSender(UUID.fromString(data.sender.id),
                new ContainerEntity(data.sender.container.host, data.sender.container.port));

        for (MessageAddress address : data.receivers) {
            message.addReceiver(UUID.fromString(address.id),
                    new ContainerEntity(address.container.host, address.container.port));
        }

        message.setType(convertType(data.type));
        message.setContent(data.content);
        return message;
    }

    public static slices.Message convertMessage(MessageEntity message) {
        slices.Message data = new slices.Message();
        data.sender = new MessageAddress(message.getSender().getAgentId().toString(),
                new ContainerAddress(message.getSender().getContainer().getHost(), message.getSender().getContainer().getPort()));

        data.receivers = new MessageAddress[message.getReceivers().size()];
        Integer index = 0;
        for (MessageEntity.Address address : message.getReceivers()) {
            data.receivers[index] = new MessageAddress(address.getAgentId().toString(),
                    new ContainerAddress(address.getContainer().getHost(), address.getContainer().getPort()));
            index++;
        }
        data.type = convertType(message.getType());
        data.content = message.getContent();
        return data;
    }

    private static MessageEntity.Type convertType(Integer type) {
        if (type == 0) {
            return MessageEntity.Type.ACCEPT_PROPOSAL;
        } else if (type == 1) {
            return MessageEntity.Type.AGREE;
        } else if (type == 2) {
            return MessageEntity.Type.CALL_FOR_PROPOSALS;
        } else if (type == 3) {
            return MessageEntity.Type.CANCEL;
        } else if (type == 4) {
            return MessageEntity.Type.CONFIRM;
        } else if (type == 5) {
            return MessageEntity.Type.DISCONFIRM;
        } else if (type == 6) {
            return MessageEntity.Type.FAILURE;
        } else if (type == 7) {
            return MessageEntity.Type.INFORM;
        } else if (type == 8) {
            return MessageEntity.Type.INFORM_IF;
        } else if (type == 9) {
            return MessageEntity.Type.INFORM_REF;
        } else if (type == 10) {
            return MessageEntity.Type.NOT_UNDERSTOOD;
        } else if (type == 11) {
            return MessageEntity.Type.PROPAGATE;
        } else if (type == 12) {
            return MessageEntity.Type.PROPOSE;
        } else if (type == 13) {
            return MessageEntity.Type.PROXY;
        } else if (type == 14) {
            return MessageEntity.Type.QUERY_IF;
        } else if (type == 15) {
            return MessageEntity.Type.QUERY_REF;
        } else if (type == 16) {
            return MessageEntity.Type.REFUSE;
        } else if (type == 17) {
            return MessageEntity.Type.REJECT_PROPOSAL;
        } else if (type == 18) {
            return MessageEntity.Type.REQUEST;
        } else if (type == 19) {
            return MessageEntity.Type.REQUEST_WHEN;
        } else if (type == 20) {
            return MessageEntity.Type.REQUEST_WHENEVER;
        } else if (type == 21) {
            return MessageEntity.Type.SUBSCRIBE;
        } else {
            return null;
        }
    }

    private static Integer convertType(MessageEntity.Type type) {
        return type.ordinal();
    }
}
