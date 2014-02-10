package cz.uhk.fim.ase.container.agents;

import cz.uhk.fim.ase.container.Container;
import cz.uhk.fim.ase.container.Registry;
import cz.uhk.fim.ase.container.agents.behaviours.InfiniteBehaviour;
import cz.uhk.fim.ase.model.MessageEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class TestAgent2 extends Agent {

    private Long bought = 0L;
    private Integer resources = 10000;
    private Integer money = 100;

    public TestAgent2(Container container) {
        super(container);
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
            MessageEntity message = new MessageEntity();
            message.setSender(getEntity().getId(), getEntity().getContainer());
            Agent random = Registry.get().getRandomAgent1();
            if (random != null) {
                message.addReceiver(random.getEntity().getId(), getEntity().getContainer());
                message.setType(MessageEntity.Type.REQUEST);
                message.setContent("i need more resources");
                send(message);
            }
        }
    }

    public class ReceiveOffer extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = receive(MessageEntity.Type.ACCEPT_PROPOSAL);
            if (message != null) {
                MessageEntity response = new MessageEntity();
                response.setType(MessageEntity.Type.AGREE);
                response.setSender(getEntity().getId(), getEntity().getContainer());
                response.addReceiver(message.getSender().getAgentId(), getEntity().getContainer());
                response.setContent(message.getContent());
                send(response);
            }
        }
    }

    public class CompleteDeal extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = receive(MessageEntity.Type.CONFIRM);
            if (message == null) {
                receive(MessageEntity.Type.REFUSE); // drop refused
                return;
            }

            resources++;
            money -= Integer.valueOf(message.getContent());
            bought++;
        }
    }
}
