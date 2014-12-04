package gossip;


import core.network.PeerConnection;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class GossipConnect {

    private Peer peer;
    private PeerConnection peerCon;
    private Double sumNodes;
    private Double weightNodes;
    private Double sumUsers;
    private Double weightUsers;
    private Double sumItems;
    private Double weightItems;
    private int id;
    private int num;
    private String username;

    static final Logger logger = LogManager.getLogger(GossipConnect.class);
    private double originalNumUsers;
    private double originalNumItems;

    //static final Logger logger = LogManager.getLogger(GossipConnect.class);
    //final Level GOSSIP = Level.forName("GOSSIP", 200);

    public GossipConnect(PeerConnection peerCon, String username, boolean isAdmin) {
        this.peerCon = peerCon;
        this.peer = peerCon.getPeer();

        resetGossip(isAdmin);
        this.id = 0;
        this.num = 0;
        this.username = username;

        peer.setObjectDataReply(new GossipReply(this));
    }

    public void sendMessage(final GossipMessage message) {


        //System.out.println("default bag size - " + new PeerMaker(new Number160(4)).getBagSize());
        List<PeerAddress> peers = peer.getPeerBean().getPeerMap().getAll();
        //System.out.println("meus peers - " + peers.size());
        if(peers.isEmpty()){
            System.out.println("Gossip nao tem vizinhos");
            return;
        }
        num++;


        List<Double> numUsersItems = peerCon.getNumUsersItems();
        if(this.originalNumUsers != numUsersItems.get(0) || this.originalNumItems != numUsersItems.get(1)){
            //novos users ou items foram adicionados entretanto, adicionar diferença ao sum
            //System.out.println("***********---**********");
            message.setSumUsers(message.getSumUsers() + (numUsersItems.get(0) - this.originalNumUsers )/2);
            message.setSumItems(message.getSumItems() + (numUsersItems.get(1) - this.originalNumItems)/2);
            originalNumUsers = numUsersItems.get(0);
            originalNumItems = numUsersItems.get(1);
        }



        final PeerAddress destinationpeer = peers.get(new Random().nextInt(((peers.size() - 1)) + 1));
        if(this.num > 50 && username.equals("Admin")) {
            message.setId(getId()+1);
            setId(getId()+1);
            this.num = 0;
            resetGossip(true);
            message.setWeightNodes(0.5);
            message.setSumNodes(0.5);
            message.setWeightUsers(0.5);
            message.setSumUsers(numUsersItems.get(0)/2);
            message.setWeightItems(0.5);
            message.setSumItems(numUsersItems.get(1)/2);

        }else{
            message.setId(getId());
        }
       /* System.out.println("1 sum: " + getSumNodes() + " weight: " + getWeightNodes());
        System.out.println("1 user sum: " + getSumUsers() + " weight: " + getWeightUsers());
        System.out.println("1 item sum: " + getSumItems() + " weight: " + getWeightItems());
*/

        this.setSumNodes(message.getSumNodes());
        this.setWeightNodes(message.getWeightNodes());

        this.setSumUsers(message.getSumUsers());
        this.setWeightUsers(message.getWeightUsers());

        this.setSumItems(message.getSumItems());
        this.setWeightItems(message.getWeightItems());

        /*System.out.println("2 sum: " + getSumNodes() + " weight: " + getWeightNodes());
        System.out.println("2 user sum: " + getSumUsers() + " weight: " + getWeightUsers());
        System.out.println("2 item sum: " + getSumItems() + " weight: " + getWeightItems());
*/


        FutureResponse response = peer.sendDirect(destinationpeer).setObject(message).start();

        response.addListener(new BaseFutureListener<BaseFuture>() {
            @Override
            public void operationComplete(BaseFuture future) throws Exception {
                //System.out.println("[GOSSIP][Sent] Sum: " + message.getSumNodes() + "; Weight: " + message.getWeightNodes());
                //System.out.println("[GOSSIP][Sent Users] Sum: " + message.getSumUsers() + "; Weight: " + message.getWeightUsers());
                //System.out.println("[GOSSIP][Sent Items] Sum: " + message.getSumItems() + "; Weight: " + message.getWeightItems());

                //if(username.equals("joao")) {
                //double numPeers = Math.round(message.getSumNodes() / message.getWeightNodes());
                double numPeers = getSumNodes()/getWeightNodes();
                //System.out.println("items antes " +  ((getSumItems() / getWeightItems())/ numPeers) );
                logger.error(numPeers);
                if(numPeers > 6)
                    numPeers = 6.0;

                //System.out.println("num Peers" + numPeers);
                double numUsers = (getSumUsers() / getWeightUsers())/ numPeers;
                double numItems = (getSumItems() / getWeightItems())/ numPeers;
                //logger.error(numItems);
                System.out.println("Wrote to log: " + numItems);

                //  }

                    /*logger.error("[GOSSIP][NODES][Sent] Sum: " + message.getSumNodes() + "; Weight: " + message.getWeightNodes() + "; ID: " + message.getId());
                    logger.error("[GOSSIP][USERS][Sent] Sum: " + message.getSumUsers() + "; Weight: " + message.getWeightUsers() + "; ID: " + message.getId());
                    logger.error("[GOSSIP][ITEMS][Sent] Sum: " + message.getSumItems() + "; Weight: " + message.getWeightItems() + "; ID: " + message.getId());
                */
            }

            @Override
            public void exceptionCaught(Throwable t) throws Exception {
            }
        });

        //num++;
    }

    public void resetGossip(boolean isAdmin){
        List<Double> numUsersItems = peerCon.getNumUsersItems();
        this.sumNodes = 1.0;
        this.sumUsers = numUsersItems.get(0);
        this.sumItems = numUsersItems.get(1);
        this.originalNumUsers = numUsersItems.get(0);
        this.originalNumItems = numUsersItems.get(1);
        if(isAdmin) {
            this.weightNodes = 1.0;
            this.weightUsers = 1.0;
            this.weightItems = 1.0;
        }else{
            this.weightNodes = 0.0;
            this.weightUsers = 0.0;
            this.weightItems = 0.0;
        }
    }

    public void resetGossip(boolean isAdmin, GossipMessage message){
        resetGossip(isAdmin);

        setSumNodes(1.0 + message.getSumNodes());
        setWeightNodes(0.0 + message.getWeightNodes());

        setSumUsers(getSumUsers()+ message.getSumUsers());
        setWeightUsers(0.0 + message.getWeightUsers());

        setSumItems(getSumItems() + message.getSumItems());
        setWeightItems(0.0 + message.getWeightItems());

    }


    public Double getSumNodes() {
        return sumNodes;
    }

    public void startGossipAdmin() {
        this.weightNodes = 1.0;
        this.weightUsers = 1.0;
        this.weightItems = 1.0;
    }

    public void setSumNodes(Double sumNodes) {
        this.sumNodes = sumNodes;
    }

    public Double getWeightNodes() {
        return weightNodes;
    }

    public void setWeightNodes(Double weightNodes) {
        this.weightNodes = weightNodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Double getSumUsers() {
        return sumUsers;
    }

    public void setSumUsers(Double sumUsers) {
        this.sumUsers = sumUsers;
    }

    public Double getWeightUsers() {
        return weightUsers;
    }

    public void setWeightUsers(Double weightUsers) {
        this.weightUsers = weightUsers;
    }

    public Double getSumItems() {
        return sumItems;
    }

    public void setSumItems(Double sumItems) {
        this.sumItems = sumItems;
    }

    public Double getWeightItems() {
        return weightItems;
    }

    public void setWeightItems(Double weightItems) {
        this.weightItems = weightItems;
    }

    public PeerConnection getPeerCon() {
        return this.peerCon;
    }

    public double getOriginalNumUsers() {
        return originalNumUsers;
    }

    public void setOriginalNumUsers(double originalNumUsers) {
        this.originalNumUsers = originalNumUsers;
    }

    public double getOriginalNumItems() {
        return originalNumItems;
    }

    public void setOriginalNumItems(double originalNumItems) {
        this.originalNumItems = originalNumItems;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
