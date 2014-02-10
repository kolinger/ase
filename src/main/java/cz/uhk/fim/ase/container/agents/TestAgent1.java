package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.container.agents.behaviours.InfiniteBehaviour;
import cz.uhk.fim.ase.model.MessageEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class TestAgent1 extends Agent {

    private Long sold = 0L;
    private Integer resources = 10000;
    private Integer money = 100;

    public TestAgent1(Container container) {
        super(container);
    }

    @Override
    protected void setup() {
        addBehaviour(new CreateOffer());
        addBehaviour(new Sell());
    }

    @Override
    public Map<String, String> getReportValues() {
        Map<String, String> values = new HashMap<String, String>();
        values.put("sold", sold.toString());
        values.put("resources", resources.toString());
        values.put("money", money.toString());
        return values;
    }

    public class CreateOffer extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = receive(MessageEntity.Type.REQUEST);
            if (message == null) {
                return;
            }

            MessageEntity response = new MessageEntity();
            response.setSender(getEntity().getId(), getEntity().getContainer());
            response.addReceiver(message.getSender().getAgentId(), getEntity().getContainer());
            response.setType(MessageEntity.Type.ACCEPT_PROPOSAL);
            Integer price = new Random().nextInt(9) + 1;
            response.setContent(price.toString());
            send(response);
        }
    }

    public class Sell extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = receive(MessageEntity.Type.AGREE);
            if (message == null) {
                return;
            }

            MessageEntity response = new MessageEntity();
            response.setSender(getEntity().getId(), getEntity().getContainer());
            response.addReceiver(message.getSender().getAgentId(), getEntity().getContainer());

            if (resources > 0) {
                response.setType(MessageEntity.Type.CONFIRM);
                response.setContent(message.getContent());
                resources--;
                money += Integer.valueOf(message.getContent());
                sold++;
            } else {
                response.setType(MessageEntity.Type.REFUSE);
                response.setContent("sorry, i don't have it");
            }

            send(response);
        }
    }
}
