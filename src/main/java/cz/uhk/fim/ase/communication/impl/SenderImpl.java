package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.common.NamedThreadFactory;
import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.communication.impl.helpers.ContextHolder;
import cz.uhk.fim.ase.communication.impl.helpers.MessagesConverter;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;
import cz.uhk.fim.ase.platform.Monitor;
import cz.uhk.fim.ase.platform.ServiceLocator;
import org.zeromq.ZMQ;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class SenderImpl implements Sender {

    private String myself = ServiceLocator.getConfig().system.myAddress;
    private Monitor monitor = ServiceLocator.getMonitor();
    private Deque<Job> queue = new ConcurrentLinkedDeque<>();

    public SenderImpl() {
        int workersCount = ServiceLocator.getConfig().system.senderWorkersCount;
        ExecutorService executor = Executors.newFixedThreadPool(workersCount, new NamedThreadFactory("sender-worker-thread"));
        for (int count = 1; count <= workersCount; count++) {
            executor.execute(new Worker());
        }
    }

    public Deque<Job> getQueue() {
        return queue;
    }

    public void send(MessageEntity message) {
        monitor.increaseSentMessagesCount(message.getReceivers().size());
        for (AgentEntity receiver : message.getReceivers()) {
            queue.addLast(new Job(receiver.getNode(), message));
        }
    }

    public void sendWelcome(WelcomeMessage message, String node) {
        queue.addLast(new Job(node, message));
    }

    public class Worker implements Runnable {

        private Map<String, ZMQ.Socket> sockets = new HashMap<>();
        private MessagesQueue messagesQueue = ServiceLocator.getMessagesQueue();

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Job job = queue.pollFirst();
                if (job != null) {
                    if (job.getAddress().equals(myself) && job.getMessage() instanceof MessageEntity) {
                        messagesQueue.addMessage((MessageEntity) job.getMessage());
                        continue;
                    }
                    byte[] data = MessagesConverter.convertObjectToBytes(job.getMessage());
                    send(job.getAddress(), data);
                }
            }
        }

        private void send(String address, byte[] data) {
            ZMQ.Socket socket = getSocket(address);
            socket.send(data, ZMQ.NOBLOCK);
        }

        private ZMQ.Socket getSocket(String address) {
            ZMQ.Socket socket;
            if (!sockets.containsKey(address)) {
                socket = createSocket(address);
                sockets.put(address, socket);
                return socket;
            }

            socket = sockets.get(address);
            if (socket == null) {
                socket = createSocket(address);
                sockets.put(address, socket);
            }

            return socket;
        }

        private ZMQ.Socket createSocket(String address) {
            ZMQ.Socket socket = ContextHolder.getContext().socket(ZMQ.PUSH);
            socket.connect("tcp://" + address);
            socket.setSendTimeOut(1000);
            socket.setReceiveTimeOut(1000);
            return socket;
        }
    }

    public class Job {
        private String address;
        private Object message;

        public Job(String address, Object message) {
            this.address = address;
            this.message = message;
        }

        public String getAddress() {
            return address;
        }

        public Object getMessage() {
            return message;
        }
    }
}
