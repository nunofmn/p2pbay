package gossip;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GossipReply implements ObjectDataReply{

    private GossipConnect gossip;
    static final Logger logger = LogManager.getLogger(GossipConnect.class);

    public GossipReply(GossipConnect gossip) {
        this.gossip = gossip;
    }

    @Override
    public Object reply(PeerAddress sender, Object request) throws Exception {
        GossipMessage message = (GossipMessage)request;
        if(message.getId() > gossip.getId()) {
            if(gossip.getUsername().equals("Admin")){

                gossip.resetGossip(true);
                gossip.setId(message.getId()+1);
            }else {
                gossip.resetGossip(false, message);
                gossip.setId(message.getId());
            }
            logger.error("Gossip reset");
            return "OK";
        } else  if(message.getId() < gossip.getId()){
            //descartar msg pois e mais antiga
            return "OK";
        }
        gossip.setSumNodes((gossip.getSumNodes() + message.getSumNodes()));
        gossip.setWeightNodes((gossip.getWeightNodes() + message.getWeightNodes()));

        gossip.setSumUsers((gossip.getSumUsers() + message.getSumUsers()));
        gossip.setWeightUsers((gossip.getWeightUsers() + message.getWeightUsers()));

        gossip.setSumItems((gossip.getSumItems() + message.getSumItems()));
        gossip.setWeightItems((gossip.getWeightItems() + message.getWeightItems()));
        return "OK";
    }
}
