package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.communication.Sender;
import cz.uhk.fim.ase.container.Registry;
import cz.uhk.fim.ase.container.agents.behaviours.InfiniteBehaviour;
import cz.uhk.fim.ase.model.MessageType;
import slices.AgentEntity;
import slices.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class TestAgent2 extends Agent {

    private Long bought = 0L;
    private Integer resources = 10000;
    private Integer money = 100;

    public TestAgent2(AgentEntity entity, Sender sender, MessagesQueue queue) {
        super(entity, sender, queue);
    }

    @Override
    protected void setup() {
        addBehaviour(new FindSellers());
        addBehaviour(new ReceiveOffer());
        addBehaviour(new CompleteDeal());
    }

    @Override
    public Map<String, String> getReportValues() {
        Map<String, String> values = new HashMap<String, String>();
        values.put("bought", bought.toString());
        values.put("resources", resources.toString());
        values.put("money", money.toString());
        return values;
    }

    public class FindSellers extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            Message message = new Message();
            message.sender = getEntity();
            AgentEntity random = Registry.get().getRandomAgent1();
            if (random != null) {
                message.receivers = new AgentEntity[]{random};
                message.type = MessageType.REQUEST.ordinal();
                message.content = "i need more resources";
                send(message);
            }
        }
    }

    public class ReceiveOffer extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            Message message = receive(MessageType.ACCEPT_PROPOSAL);
            if (message != null) {
                Message response = new Message();
                response.type = MessageType.AGREE.ordinal();
                response.sender = getEntity();
                response.receivers = new AgentEntity[]{message.sender};
                response.content = message.content;
                send(response);
            }
        }
    }

    public class CompleteDeal extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            Message message = receive(MessageType.CONFIRM);
            if (message == null) {
                receive(MessageType.REFUSE); // drop refused
                return;
            }

            resources++;
            money -= Integer.valueOf(message.content);
            bought++;
        }
    }
}
