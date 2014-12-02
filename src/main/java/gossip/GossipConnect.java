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
    private String username;

    //static final Logger logger = LogManager.getLogger(GossipConnect.class);
    //final Level GOSSIP = Level.forName("GOSSIP", 200);

    public GossipConnect(Peer peer, String username, boolean isAdmin) {
        this.peer = peer;
        this.sum = 1.0;
        this.weight = 0.0;
        if(isAdmin)
            this.weight = 1.0;
        this.id = 0;
        this.num = 0;
        this.username = username;

        peer.setObjectDataReply(new GossipReply(this));
    }

    public void sendMessage(final GossipMessage message) {





            List<PeerAddress> peers = peer.getPeerBean().getPeerMap().getAll();
        if(peers.isEmpty()){
            System.out.println("Gossip nao tem vizinhos");
            return;
        }
        num++;

            final PeerAddress destinationpeer = peers.get(new Random().nextInt(((peers.size() - 1)) + 1));
        if(this.num > 10 && username.equals("Admin")) {
            message.setId(getId()+1);
            setId(getId()+1);
            this.num = 0;
            //this.setSum(0.5);
            //this.setWeight(0.5);
            message.setWeight(0.5);
            message.setSum(0.5);

        }else{
            message.setId(getId());
        }
        this.setSum(message.getSum());
        this.setWeight(message.getWeight());


            FutureResponse response = peer.sendDirect(destinationpeer).setObject(message).start();

            response.addListener(new BaseFutureListener<BaseFuture>() {
                @Override
                public void operationComplete(BaseFuture future) throws Exception {
                    System.out.println("[GOSSIP][Sent] Sum: " + message.getSum() + "; Weight: " + message.getWeight());
                    //logger.log(GOSSIP, "Message sent to " + destinationpeer.getID().longValue());
                    // System.out.println("Message sent to " + destinationpeer.getID());
                }

                @Override
                public void exceptionCaught(Throwable t) throws Exception {
                }
            });

            //num++;


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
