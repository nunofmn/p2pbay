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
    //static final Logger logger = LogManager.getLogger(GossipConnect.class);
    //final Level GOSSIP = Level.forName("GOSSIP", 200);

    public GossipConnect(Peer peer) {
        this.peer = peer;
        peer.setObjectDataReply(new ObjectDataReply() {
            @Override
            public Object reply(PeerAddress sender, Object request) throws Exception {
                //logger.log(GOSSIP, "Received message from: " + sender.getID().longValue());
                System.out.print("Received message from: " + sender.getID().toString());
                return "OK";
            }
        });
    }

    public void sendMessage(String message) {

        List<PeerAddress> peers = peer.getPeerBean().getPeerMap().getAll();
        final PeerAddress destinationpeer = peers.get(new Random().nextInt(((peers.size()-1) - 1) + 1) + 1);

        FutureResponse response = peer.sendDirect(destinationpeer).setObject(message).start();

        response.addListener(new BaseFutureListener<BaseFuture>() {
            @Override
            public void operationComplete(BaseFuture future) throws Exception {
                //logger.log(GOSSIP, "Message sent to " + destinationpeer.getID().longValue());
                System.out.println("Message sent to " + destinationpeer.getID());
            }

            @Override
            public void exceptionCaught(Throwable t) throws Exception {
            }
        });

    }


}
