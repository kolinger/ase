package cz.uhk.fim.ase.platform.agents;

import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.MessageType;
import cz.uhk.fim.ase.model.impl.MessageEntityImpl;
import cz.uhk.fim.ase.platform.ServiceLocator;
import cz.uhk.fim.ase.platform.agents.behaviours.InfiniteBehaviour;

import java.util.Map;

/**
 * Benchmark agent - testing communication between nodes
 *
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class BenchmarkAgent extends Agent {

    public BenchmarkAgent(AgentEntity entity) {
        super(entity);
    }

    @Override
    protected void setup() {
        addBehaviour(new Send());
        addBehaviour(new Receive());
    }

    @Override
    public Map<String, String> getReportValues() {
        return null;
    }

    public class Send extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = new MessageEntityImpl();
            message.setSender(getEntity());
            message.getReceivers().add(ServiceLocator.getRegistry().getRandomForeignByType("benchmark"));
            message.setType(MessageType.REQUEST);
            message.setContent("hello world!");
            send(message);
        }
    }

    public class Receive extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            receive(MessageType.REQUEST);
        }
    }
}
