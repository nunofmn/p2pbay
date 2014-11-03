package core.network;

import core.model.NetworkContent;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import java.io.IOException;
import java.util.Random;

public class DataManager {

    private final PeerConnection peerconnection;

    public DataManager(PeerConnection peerconnection) {
       this.peerconnection = peerconnection;
    }

    private Peer getPeer() {
        return this.peerconnection.getPeer();

    }

    public void put(NetworkContent content) {

        Number160 key = new Number160(new Random());

        try {
            Data data = new Data(content);
            final FutureDHT futureDHT = peerconnection.getPeer().put(key).setData(data).start();

            futureDHT.addListener(new BaseFutureAdapter<BaseFuture>() {
                @Override
                public void operationComplete(BaseFuture baseFuture) throws Exception {

                    if(futureDHT.isSuccess()) {
                        System.out.println("File put success");
                    }else{
                        System.out.println("File put failure");
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void get(Number160 key, BaseFutureAdapter<BaseFuture> future) {

        final FutureDHT futureDHT = peerconnection.getPeer().get(key).start();
        futureDHT.addListener(future);

    }

}
