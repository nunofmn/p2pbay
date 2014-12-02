package gossip;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

import java.util.List;

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
            if(gossip.getUsername().equals("Admin")){

                gossip.resetGossip(true);
                gossip.setId(message.getId()+1);
                System.out.println("Gossip -> sum: " + message.getSumNodes() + "; weight: " + message.getWeightNodes() + "; id: " + message.getId());
            }else {
                gossip.resetGossip(false, message);


                gossip.setId(message.getId());
                System.out.println("Gossip -> sum: " + message.getSumNodes() + "; weight: " + message.getWeightNodes() + "; id: " + message.getId());
            }


            return "OK";
        }
        System.out.println("Gossip -> sum: " + message.getSumNodes() + "; weight: " + message.getWeightNodes() + "; id: " + message.getId());

        System.out.println("Tinha sum: " + gossip.getSumNodes() + " weight: " + gossip.getWeightNodes());
        System.out.println("Tinha user sum: " + gossip.getSumUsers() + " weight: " + gossip.getWeightUsers());
        System.out.println("Tinha item sum: " + gossip.getSumItems() + " weight: " + gossip.getWeightItems());

        gossip.setSumNodes((gossip.getSumNodes() + message.getSumNodes()));
        gossip.setWeightNodes((gossip.getWeightNodes() + message.getWeightNodes()));

        gossip.setSumUsers((gossip.getSumUsers() + message.getSumUsers()));
        gossip.setWeightUsers((gossip.getWeightUsers() + message.getWeightUsers()));

        gossip.setSumItems((gossip.getSumItems() + message.getSumItems()));
        gossip.setWeightItems((gossip.getWeightItems() + message.getWeightItems()));
        System.out.println("Fiquei sum: " + gossip.getSumNodes() + " weight: " + gossip.getWeightNodes());
        System.out.println("Fiquei user sum: " + gossip.getSumUsers() + " weight: " + gossip.getWeightUsers());
        System.out.println("Fiquei item sum: " + gossip.getSumItems() + " weight: " + gossip.getWeightItems());

        return "OK";
    }
}
