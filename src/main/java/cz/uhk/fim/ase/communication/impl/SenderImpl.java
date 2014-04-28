package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.communication.impl.helpers.ContextHolder;
import cz.uhk.fim.ase.communication.impl.helpers.MessagesConverter;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class SenderImpl implements Sender {

    private static Logger logger = LoggerFactory.getLogger(SenderImpl.class);
    int maxConnectionPerNode = 10;
    private Map<String, Worker[]> workers = new ConcurrentHashMap<>();

    public void send(MessageEntity message) {
        byte[] bytes = MessagesConverter.convertObjectToBytes(message);

        for (AgentEntity receiver : message.getReceivers()) {
            Worker worker = getWorker(receiver.getNode());
            while (worker == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // ignore
                }
                worker = getWorker(receiver.getNode());
            }
            worker.send(bytes);
        }
    }

    public void sendWelcome(WelcomeMessage message, String node) {
        Worker worker = getWorker(node);
        while (worker == null) {
            worker = getWorker(node);
        }
        worker.send(MessagesConverter.convertObjectToBytes(message));
    }

    private synchronized Worker getWorker(String address) {
        Worker[] queue;

        if (!workers.containsKey(address)) {
            queue = new Worker[maxConnectionPerNode];
            queue[0] = new Worker(address);
            workers.put(address, queue);
            return queue[0];
        }

        queue = workers.get(address);
        int number;
        for (number = 0; number < maxConnectionPerNode; number++) {
            if (queue[number] != null && queue[number].isReady()) {
                return queue[number];
            }
        }

        if (queue.length < maxConnectionPerNode) {
            queue[number] = new Worker(address);
            workers.put(address, queue);
            return queue[number];
        }

        return null;
    }

    public class Worker {

        private boolean ready = true;
        private String address;
        private ZMQ.Socket socket = ContextHolder.getContext().socket(ZMQ.PUSH);

        public Worker(String address) {
            this.address = address;
            connect();
        }

        public synchronized boolean isReady() {
            return ready;
        }

        public void send(byte[] bytes) {
            ready = false;
            if (!socket.send(bytes, 0)) {
                // connection failed - reconnect
                socket.close();
                connect();
                socket.send(bytes, 0);
                logger.error("Connection failed - reconnect");
            }
            ready = true;
        }

        private void connect() {
            socket.connect("tcp://" + address);
            socket.setSendTimeOut(1000);
            socket.setReceiveTimeOut(1000);
        }
    }
}
