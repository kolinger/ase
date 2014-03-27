package cz.uhk.fim.ase.communication.impl.helpers;

import cz.uhk.fim.ase.model.ByeMessage;
import cz.uhk.fim.ase.model.HelloMessage;
import cz.uhk.fim.ase.model.SyncMessage;
import cz.uhk.fim.ase.model.WelcomeMessage;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * MessagesConverter is internal helper for ZeroMQ communication layer - converting bytes to objects and objects to
 * bytes with serialisation.
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesConverter {

    public static Object convertBytesToObject(byte[] bytes) {
        bytes = Arrays.copyOfRange(bytes, 1, bytes.length); // remove first byte

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException e) {
            LoggerFactory.getLogger(MessagesConverter.class).warn("Message converting failed", e);
            return null;
        } catch (ClassNotFoundException e) {
            LoggerFactory.getLogger(MessagesConverter.class).warn("Message converting failed", e);
            return null;
        }
    }

    public static byte[] convertObjectToBytes(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream ous = new ObjectOutputStream(bos);
            ous.writeObject(object);
            byte[] objectBytes = bos.toByteArray();

            // resize array, first byte is type
            byte[] bytes = new byte[objectBytes.length + 1]; // + 1 for type
            System.arraycopy(objectBytes, 0, bytes, 1, objectBytes.length);

            // fit first byte with type, default is 0 so type = direct message
            if (object instanceof HelloMessage) {
                bytes[0] = 1;
            } else if (object instanceof WelcomeMessage) {
                bytes[0] = 2;
            } else if (object instanceof SyncMessage) {
                bytes[0] = 3;
            } else if (object instanceof ByeMessage) {
                bytes[0] = 4;
            }

            return bytes;
        } catch (IOException e) {
            LoggerFactory.getLogger(MessagesConverter.class).warn("Message converting failed", e);
            return null;
        }
    }
}
