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
        //System.out.println("Gossip -> sum: " + message.getSum() + "; weight: " + message.getWeight() + "; id: " + message.getId());

        gossip.setSum((gossip.getSum() + message.getSum())/2);
        gossip.setWeight((gossip.getWeight() + message.getWeight())/2);
        gossip.sendMessage(new GossipMessage(gossip.getSum(), gossip.getWeight(), gossip.getId()));

        return "OK";
    }
}
