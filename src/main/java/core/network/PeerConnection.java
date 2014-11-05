package core.network;

import core.model.NetworkContent;
import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Random;

public class PeerConnection {

    final private Peer peer;

    public PeerConnection( String myPort )
            throws Exception {

        Random r = new Random();
        peer = new PeerMaker(new Number160(r)).setPorts(Integer.parseInt(myPort)).makeAndListen();
        FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(Integer.parseInt(myPort)).start();
        fb.awaitUninterruptibly();
        if (fb.getBootstrapTo() != null) {
            peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }

    }


    public PeerConnection(String ipOtherPeer, String otherPeerPort, String myPort )
            throws Exception {

        Random r = new Random();
        Bindings b = new Bindings();
        //b.addInterface("eth0");
// create a peer with a random peerID, on port 4000, listening to the interface eth0
        peer = new PeerMaker(new Number160(r)).setPorts(Integer.parseInt(myPort)).setBindings(b).makeAndListen();
        peer.getConfiguration().setBehindFirewall(true);

        InetAddress address = Inet4Address.getByName(ipOtherPeer);
        FutureDiscover futureDiscover = peer.discover().setInetAddress( address ).setPorts(Integer.parseInt(otherPeerPort) ).start();
        futureDiscover.awaitUninterruptibly();
        FutureBootstrap futureBootstrap = peer.bootstrap().setInetAddress( address ).setPorts( Integer.parseInt(otherPeerPort)).start();
        futureBootstrap.awaitUninterruptibly();

        if(futureBootstrap.getBootstrapTo() != null) {
            peer.discover().setPeerAddress(futureBootstrap.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    public Peer getPeer() {
        return this.peer;
    }

    public NetworkContent get(String name)
        throws ClassNotFoundException, IOException {

        FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
        futureDHT.awaitUninterruptibly();
        if(futureDHT.isSuccess()) {
            return (NetworkContent)futureDHT.getData().getObject();
        }

        return null;
    }

    public void store(String name, NetworkContent object)
        throws IOException {

        peer.put(Number160.createHash(name)).setData(new Data(object)).start().awaitUninterruptibly();
    }

}
