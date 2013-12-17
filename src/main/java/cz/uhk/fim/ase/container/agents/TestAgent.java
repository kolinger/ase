package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.container.agents.behaviours.CyclicBehaviour;
import cz.uhk.fim.ase.container.agents.behaviours.TickerBehaviour;
import cz.uhk.fim.ase.model.MessageEntity;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class TestAgent extends Agent {

    public TestAgent(Container container) {
        super(container);
    }

    @Override
    protected void setup() {
        addBehaviour(new Offer());
        addBehaviour(new Request());
    }

    public class Offer extends CyclicBehaviour {

        @Override
        protected void doCycle() {
            receive();
        }
    }

    public class Request extends TickerBehaviour {

        @Override
        protected void doTick() {
            MessageEntity message = new MessageEntity();
            message.setSender(getEntity().getId(), getEntity().getContainer());
            message.addReceiver(getEntity().getId(), getEntity().getContainer());
            message.setType(MessageEntity.Type.REQUEST);
            message.setContent("hi mate :)");
            send(message);
        }
    }
}
