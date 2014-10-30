import core.network.PeerConnection;

public class Main {

    public static void main(String[] args)
            throws NumberFormatException, Exception {

        PeerConnection peercore = new PeerConnection(Integer.parseInt(args[0]));

        if(args.length == 3) {
            peercore.store(args[1], args[2]);
        }

        if(args.length == 2) {
           System.out.println("Name:" + args[1] + " IP:" + peercore.get(args[1]));
        }

        System.out.println("P2PBay - Coming Soon");

    }

}
