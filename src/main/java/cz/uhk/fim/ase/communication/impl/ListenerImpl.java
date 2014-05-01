package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.communication.Listener;
import cz.uhk.fim.ase.communication.impl.helpers.ContextHolder;
import cz.uhk.fim.ase.communication.impl.helpers.MessagesConverter;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.WelcomeMessage;
import cz.uhk.fim.ase.platform.Monitor;
import cz.uhk.fim.ase.platform.Registry;
import cz.uhk.fim.ase.platform.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class ListenerImpl implements Listener {

    private Boolean ready = false;
    private ZMQ.Socket listener;
    private Logger logger = LoggerFactory.getLogger(ListenerImpl.class);
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            bind();
        }
    }, "listener-thread");
    private Monitor monitor = ServiceLocator.getMonitor();

    public void shutdown() {
        thread.interrupt();
    }

    @Override
    public synchronized Boolean isReady() {
        return ready;
    }

    public void listen() {
        thread.start();
    }

    private void bind() {
        String address = ServiceLocator.getConfig().system.listenerAddress;

        bind(address);
        logger.info("Listen on " + address);

        ZMQ.Socket broker = ContextHolder.getContext().socket(ZMQ.PUSH);
        broker.bind("inproc://listener-workers");

        int workersCount = ServiceLocator.getConfig().system.listenerWorkersCount;
        Thread[] threads = new Thread[workersCount];
        for (int count = 0; count < workersCount; count++) {
            threads[count] = new Thread(new Worker(), "listener-worker-thread" + (count + 1));
            threads[count].start();
        }

        while (!Thread.currentThread().isInterrupted()) {
            ready = true;
            byte[] bytes = listener.recv(0);
            if (bytes == null || bytes.length == 0) { // connection failed?
                continue;
            }
            broker.send(bytes, 0);
        }

        broker.close();
        listener.close();
    }

    private void bind(String address) {
        listener = ContextHolder.getContext().socket(ZMQ.PULL);
        listener.bind("tcp://" + address);
        listener.setSendTimeOut(1000);
        listener.setReceiveTimeOut(1000);
    }

    private void handleBytes(byte[] bytes) {
        byte type = bytes[0]; // first byte is message type

        if (type == 0) { // direct
            MessageEntity message = (MessageEntity) MessagesConverter.convertBytesToObject(bytes);
            if (message != null) {
                monitor.increaseReceivedMessagesCount(1);
                ServiceLocator.getMessagesQueue().addMessage(message);
            }
        } else if (type == 2) { // welcome
            WelcomeMessage welcomeMessage = (WelcomeMessage) MessagesConverter.convertBytesToObject(bytes);
            if (welcomeMessage != null) {
                for (AgentEntity agent : welcomeMessage.getAgents()) {
                    ServiceLocator.getRegistry().register(agent);
                }
                ServiceLocator.getSyncService().updateNodeState(welcomeMessage.getNode(), welcomeMessage.getTick());
            }
        }
    }

    public class Worker implements Runnable {

        public void run() {
            ZMQ.Socket worker = ContextHolder.getContext().socket(ZMQ.PULL);
            worker.connect("inproc://listener-workers");

            while (!Thread.currentThread().isInterrupted()) {
                byte[] bytes = worker.recv(0);
                if (bytes == null || bytes.length == 0) {
                    continue;
                }
                handleBytes(bytes);
            }
        }
    }
}
