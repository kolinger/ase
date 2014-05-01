package cz.uhk.fim.ase.platform;

import java.rmi.registry.*;

/**
 * Dummy storage for monitoring and system statistics
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class Monitor {

    private long tasksCount = 0;
    private long sentMessagesCount = 0;
    private long receivedMessagesCount = 0;

    public void increaseTasksCount(long count) {
        tasksCount += count;
    }

    public Long getTasksCount() {
        return tasksCount;
    }

    public void increaseSentMessagesCount(long count) {
        sentMessagesCount += count;
    }

    public Long getSentMessagesCount() {
        return sentMessagesCount;
    }

    public void increaseReceivedMessagesCount(long count) {
        receivedMessagesCount += count;
    }

    public Long getReceivedMessagesCount() {
        return receivedMessagesCount;
    }

    public Long getQueueSize() {
        return ServiceLocator.getMessagesQueue().getSize();
    }

    public Long getTotalMemory() {
        return Runtime.getRuntime().maxMemory() == Long.MAX_VALUE ? -1 : Runtime.getRuntime().maxMemory();
    }

    public Long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public Long getPlatformAgentsCount() {
        return (long) ServiceLocator.getRegistry().getLocalAgents().size();
    }

    public Long getTotalAgentsCount() {
        return (long) ServiceLocator.getRegistry().getAgents().size();
    }
}
