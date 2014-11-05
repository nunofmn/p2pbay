import client.menu.LoginMenu;
import core.model.Item;
import core.model.NetworkContent;
import core.model.UserProfile;
import core.network.PeerConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args)
            throws NumberFormatException, Exception {

        String user;
        String pass;
        NetworkContent userProfile;
        PeerConnection peercore = null;
        if (args.length == 3) {
            peercore = new PeerConnection(args[0], args[1], args[2]);
        }
        if (args.length == 1) {
            peercore = new PeerConnection(args[0]);
        }
        LoginMenu m = new LoginMenu();
        m.display(peercore);

    }

}
