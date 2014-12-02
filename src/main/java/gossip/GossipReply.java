package gossip;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class GossipReply implements ObjectDataReply{

    private GossipConnect gossip;

    public GossipReply(GossipConnect gossip) {
        this.gossip = gossip;
    }

    @Override
    public Object reply(PeerAddress sender, Object request) throws Exception {
        //logger.log(GOSSIP, "Received message from: " + sender.getID().longValue());
        GossipMessage message = (GossipMessage)request;
        if(message.getId() > gossip.getId()) {

            System.out.println("Mudou o id, reset! " + gossip.getId() + "|" + message.getId());
            gossip.setSum(1.0 + message.getSum());
            gossip.setWeight(0.0 +  message.getWeight());
            gossip.setId(message.getId());
            System.out.println("Gossip -> sum: " + message.getSum() + "; weight: " + message.getWeight() + "; id: " + message.getId());

            return "OK";
        }
        System.out.println("Gossip -> sum: " + message.getSum() + "; weight: " + message.getWeight() + "; id: " + message.getId());

        System.out.println("Tinha sum: " + gossip.getSum() + " weight: " + gossip.getWeight());
        gossip.setSum((gossip.getSum() + message.getSum()));
        gossip.setWeight((gossip.getWeight() + message.getWeight()));
        System.out.println("Fiquei sum: " + gossip.getSum() + " weight: " + gossip.getWeight());

        return "OK";
    }
}
