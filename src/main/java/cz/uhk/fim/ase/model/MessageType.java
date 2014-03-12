package cz.uhk.fim.ase.model;

/**
 * We use constant integers over enum because its easier to transfer that object over network.
 *
 * @link http://jmvidal.cse.sc.edu/talks/agentcommunication/performatives.html
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public interface MessageType {
    final public static Integer ACCEPT_PROPOSAL = 0;
    final public static Integer AGREE = 1;
    final public static Integer CANCEL = 2;
    final public static Integer CALL_FOR_PROPOSALS = 3;
    final public static Integer CONFIRM = 4;
    final public static Integer DISCONFIRM = 5;
    final public static Integer FAILURE = 6;
    final public static Integer INFORM = 7;
    final public static Integer INFORM_IF = 8;
    final public static Integer INFORM_REF = 9;
    final public static Integer NOT_UNDERSTOOD = 10;
    final public static Integer PROPAGATE = 11;
    final public static Integer PROPOSE = 12;
    final public static Integer PROXY = 13;
    final public static Integer QUERY_IF = 14;
    final public static Integer QUERY_REF = 15;
    final public static Integer REFUSE = 16;
    final public static Integer REJECT_PROPOSAL = 17;
    final public static Integer REQUEST = 18;
    final public static Integer REQUEST_WHEN = 19;
    final public static Integer REQUEST_WHENEVER = 20;
    final public static Integer SUBSCRIBE = 21;
}
