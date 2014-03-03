/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
module slices {
    // model
    dictionary<string, string> StringsMap;

    struct AgentEntity {
        string container;
        string id;
        StringsMap properties;
    };
    sequence<AgentEntity> AgentsList;

    // discovering
    interface DiscoverReply {
        void reply(Object* obj);
    };

    interface Discover {
        void lookup(DiscoverReply* reply);
    };

    interface DiscoverHello {
        string getInstanceId();
        AgentsList getAgents();
    };

    // messages
    struct MessageAddress {
        string id;
        string container;
    };

    sequence<MessageAddress> MessageReceivers;

    struct Message {
        AgentEntity sender;
        AgentsList receivers;
        int type;
        string content;
    };

    interface MessageTransporter {
        void transport(Message data);
    };
};