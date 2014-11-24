package gossip;


import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class GossipConnect {

    private Peer peer;
    private Double sum;
    private Double weight;
    private int id;
    private int num;

    //static final Logger logger = LogManager.getLogger(GossipConnect.class);
    //final Level GOSSIP = Level.forName("GOSSIP", 200);

    public GossipConnect(Peer peer) {
        this.peer = peer;
        this.sum = 1.0;
        this.weight = 0.0;
        this.id = 0;
        this.num = 0;

        peer.setObjectDataReply(new GossipReply(this));
    }

    public void sendMessage(GossipMessage message) {

        if(this.num < 20) {

            List<PeerAddress> peers = peer.getPeerBean().getPeerMap().getAll();
            final PeerAddress destinationpeer = peers.get(new Random().nextInt(((peers.size() - 1)) + 1));

            FutureResponse response = peer.sendDirect(destinationpeer).setObject(message).start();

            response.addListener(new BaseFutureListener<BaseFuture>() {
                @Override
                public void operationComplete(BaseFuture future) throws Exception {
                    //logger.log(GOSSIP, "Message sent to " + destinationpeer.getID().longValue());
                    // System.out.println("Message sent to " + destinationpeer.getID());
                }

                @Override
                public void exceptionCaught(Throwable t) throws Exception {
                }
            });

            num++;
        }

    }

    public Double getSum() {
        return sum;
    }

    public void startGossip() {
        this.weight = 1.0;
    }

    public void setSum(Double sum) {
             this.sum = sum;
         }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
