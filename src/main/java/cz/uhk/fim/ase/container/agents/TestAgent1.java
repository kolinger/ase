package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.MessagesSender;
import cz.uhk.fim.ase.container.agents.behaviours.InfiniteBehaviour;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.MessageType;
import cz.uhk.fim.ase.model.impl.MessageEntityImpl;

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

    public TestAgent1(AgentEntity entity, MessagesSender sender, MessagesQueue queue) {
        super(entity, sender, queue);
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
            MessageEntity message = receive(MessageType.REQUEST);
            if (message == null) {
                return;
            }

            MessageEntity response = MessageEntityImpl.createResponse(message);
            response.setSender(getEntity());
            response.setType(MessageType.ACCEPT_PROPOSAL);
            Integer price = new Random().nextInt(9) + 1;
            response.setContent(price.toString());
            send(response);
        }
    }

    public class Sell extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = receive(MessageType.AGREE);
            if (message == null) {
                return;
            }

            MessageEntity response = MessageEntityImpl.createResponse(message);
            response.setSender(getEntity());

            if (resources > 0) {
                response.setType(MessageType.CONFIRM);
                response.setContent(message.getContent());
                resources--;
                money += Integer.valueOf(message.getContent());
                sold++;
            } else {
                response.setType(MessageType.REFUSE);
                response.setContent("sorry, i don't have it");
            }

            send(response);
        }
    }
}
