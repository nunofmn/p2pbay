package gossip;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GossipReply implements ObjectDataReply{

    private GossipConnect gossip;
    static final Logger logger = LogManager.getLogger(GossipReply.class);

    public GossipReply(GossipConnect gossip) {
        this.gossip = gossip;
    }

    @Override
    public Object reply(PeerAddress sender, Object request) throws Exception {
        //logger.log(GOSSIP, "Received message from: " + sender.getID().longValue());
        GossipMessage message = (GossipMessage)request;
        if(message.getId() > gossip.getId()) {


            logger.error("[GOSSIP][Reset] New: " + gossip.getId() + "; Old: " + message.getId());

            if(gossip.getUsername().equals("Admin")){

                gossip.resetGossip(true);
                gossip.setId(message.getId()+1);
                logger.error("[GOSSIP][NODES][Received] Sum: " + message.getSumNodes() + "; Weight: " + message.getWeightNodes() + "; ID: " + message.getId());

            }else {
                gossip.resetGossip(false, message);
                gossip.setId(message.getId());
                logger.error("[GOSSIP][NODES][Received] Sum: " + message.getSumNodes() + "; Weight: " + message.getWeightNodes() + "; ID: " + message.getId());
            }


            return "OK";
        }
        //System.out.println("Gossip -> sum: " + message.getSumNodes() + "; weight: " + message.getWeightNodes() + "; id: " + message.getId());

        //System.out.println("Tinha sum: " + gossip.getSumNodes() + " weight: " + gossip.getWeightNodes());
        //System.out.println("Tinha user sum: " + gossip.getSumUsers() + " weight: " + gossip.getWeightUsers());
        //System.out.println("Tinha item sum: " + gossip.getSumItems() + " weight: " + gossip.getWeightItems());
        logger.error("[GOSSIP][NODES][Received] Sum: " + message.getSumNodes() + "; Weight: " + message.getWeightNodes() + "; ID: " + message.getId());
        logger.error("[GOSSIP][USERS][Received] Sum: " + message.getSumUsers() + "; Weight: " + message.getWeightUsers() + "; ID: " + message.getId());
        logger.error("[GOSSIP][ITEMS][Received] Sum: " + message.getSumItems() + "; Weight: " + message.getWeightItems() + "; ID: " + message.getId());

        gossip.setSumNodes((gossip.getSumNodes() + message.getSumNodes()));
        gossip.setWeightNodes((gossip.getWeightNodes() + message.getWeightNodes()));

        gossip.setSumUsers((gossip.getSumUsers() + message.getSumUsers()));
        gossip.setWeightUsers((gossip.getWeightUsers() + message.getWeightUsers()));

        gossip.setSumItems((gossip.getSumItems() + message.getSumItems()));
        gossip.setWeightItems((gossip.getWeightItems() + message.getWeightItems()));
        //System.out.println("Fiquei sum: " + gossip.getSumNodes() + " weight: " + gossip.getWeightNodes());
        //System.out.println("Fiquei user sum: " + gossip.getSumUsers() + " weight: " + gossip.getWeightUsers());
        //System.out.println("Fiquei item sum: " + gossip.getSumItems() + " weight: " + gossip.getWeightItems());

        return "OK";
    }
}
