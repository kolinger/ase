/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
module slices {
    // container
    struct ContainerAddress {
        string host;
        int port;
    };

    // message
    struct MessageAddress {
        string id;
        ContainerAddress container;
    };

    sequence<MessageAddress> MessageReceivers;

    struct Message {
        MessageAddress sender;
        MessageReceivers receivers;
        int type;
        string content;
    };

    interface MessageTransporter {
        void transport(Message data);
    };
};