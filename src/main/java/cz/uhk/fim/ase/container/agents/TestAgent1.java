package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.container.agents.behaviours.InfiniteBehaviour;
import cz.uhk.fim.ase.model.MessageType;
import slices.AgentEntity;
import slices.Message;

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

    public TestAgent1(AgentEntity entity, Sender sender, MessagesQueue queue) {
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
            Message message = receive(MessageType.REQUEST);
            if (message == null) {
                return;
            }

            Message response = new Message();
            response.sender = getEntity();
            response.receivers = new AgentEntity[]{message.sender};
            response.type = MessageType.ACCEPT_PROPOSAL.ordinal();
            Integer price = new Random().nextInt(9) + 1;
            response.content = price.toString();
            send(response);
        }
    }

    public class Sell extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            Message message = receive(MessageType.AGREE);
            if (message == null) {
                return;
            }

            Message response = new Message();
            response.sender = getEntity();
            response.receivers = new AgentEntity[]{message.sender};

            if (resources > 0) {
                response.type = MessageType.CONFIRM.ordinal();
                response.content = message.content;
                resources--;
                money += Integer.valueOf(message.content);
                sold++;
            } else {
                response.type = MessageType.REFUSE.ordinal();
                response.content = "sorry, i don't have it";
            }

            send(response);
        }
    }
}
