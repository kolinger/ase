/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */

[["java:package:cz.uhk.fim.ase.model"]]
module internal {
    dictionary<string, string> StringsMap;

    struct AgentEntity {
        string container;
        string id;
        StringsMap properties;
    };

    sequence<AgentEntity> AgentsList;

    struct MessageEntity {
        AgentEntity sender;
        AgentsList receivers;
        int type;
        string content;
    };
};
