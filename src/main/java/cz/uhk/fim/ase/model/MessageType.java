package cz.uhk.fim.ase.model;

/**
 * @link http://jmvidal.cse.sc.edu/talks/agentcommunication/performatives.html
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public enum MessageType {
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
