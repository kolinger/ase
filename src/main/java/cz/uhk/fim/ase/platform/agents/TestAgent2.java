package cz.uhk.fim.ase.platform.agents;

import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.model.MessageType;
import cz.uhk.fim.ase.model.impl.MessageEntityImpl;
import cz.uhk.fim.ase.platform.ServiceLocator;
import cz.uhk.fim.ase.platform.agents.behaviours.InfiniteBehaviour;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class TestAgent2 extends Agent {

    private Long bought = 0L;
    private Integer resources = 10000;
    private Integer money = 100;

    public TestAgent2(AgentEntity entity) {
        super(entity);
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
            MessageEntity message = new MessageEntityImpl();
            message.setSender(getEntity());
            AgentEntity random = ServiceLocator.getRegistry().getRandomByType("1");
            if (random != null) {
                message.getReceivers().add(random);
                message.setType(MessageType.REQUEST);
                message.setContent("i need more resources");
                send(message);
            }
        }
    }

    public class ReceiveOffer extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = receive(MessageType.ACCEPT_PROPOSAL);
            if (message != null) {
                MessageEntity response = MessageEntityImpl.createResponse(message);
                response.setType(MessageType.AGREE);
                message.setSender(getEntity());
                response.setContent(message.getContent());
                send(response);
            }
        }
    }

    public class CompleteDeal extends InfiniteBehaviour {

        @Override
        protected void doCycle() {
            MessageEntity message = receive(MessageType.CONFIRM);
            if (message == null) {
                receive(MessageType.REFUSE); // drop refused
                return;
            }

            resources++;
            money -= Integer.valueOf(message.getContent());
            bought++;
        }
    }
}
