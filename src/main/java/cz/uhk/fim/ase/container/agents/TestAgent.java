package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.container.agents.behaviours.InfiniteBehaviour;
import cz.uhk.fim.ase.model.MessageEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class TestAgent extends Agent {

    private Long offerCount = 0L;
    private Long requestCount = 0L;

    public TestAgent(Container container) {
        super(container);
    }

    @Override
    protected void setup() {
        addBehaviour(new Offer());
        addBehaviour(new Request());
    }

    @Override
    public Map<String, String> getReportValues() {
        Map<String, String> values = new HashMap<String, String>();
        values.put("offers", offerCount.toString());
        values.put("requests", requestCount.toString());
        return values;
    }

    public class Offer extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            receive();
            offerCount++;
        }
    }

    public class Request extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = new MessageEntity();
            message.setSender(getEntity().getId(), getEntity().getContainer());
            message.addReceiver(getEntity().getId(), getEntity().getContainer());
            message.setType(MessageEntity.Type.REQUEST);
            message.setContent("hi mate :)");
            send(message);
            requestCount++;
        }
    }
}
