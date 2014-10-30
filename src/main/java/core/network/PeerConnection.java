package core.network;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import java.io.IOException;

public class PeerConnection {

    final private Peer peer;

    public PeerConnection(int peerId)
            throws Exception {

        peer = new PeerMaker(Number160.createHash(peerId)).setPorts(4000+peerId).makeAndListen();
        FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(4001).start();
        fb.awaitUninterruptibly();

        if(fb.getBootstrapTo() != null) {
            peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    public String get(String name)
        throws ClassNotFoundException, IOException {

        FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
        futureDHT.awaitUninterruptibly();
        if(futureDHT.isSuccess()) {
            return futureDHT.getData().getObject().toString();
        }

        return "not found";
    }

    public void store(String name, String ip)
        throws IOException {

        peer.put(Number160.createHash(name)).setData(new Data(ip)).start().awaitUninterruptibly();
    }

}
