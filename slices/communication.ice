/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */

#include <model.ice>

[["java:package:cz.uhk.fim.ase.communication.impl"]]
module internal {
    interface GlobalReply {
        void reply(Object * obj);
    };

    interface GlobalHandler {
        void lookup(GlobalReply * reply);
    };

    interface GlobalHelloMessage {
        string getInstanceId();
        AgentsList getAgents();
    };

    interface MessagesHandler {
        void handle(MessageEntity message);
    };
};