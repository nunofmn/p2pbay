package client.menu;

import core.model.Item;
import core.model.NetworkContent;
import core.model.UserProfile;
import core.network.PeerConnection;
import net.tomp2p.peers.Number160;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by EngSoftware on 05-11-2014.
 */
public class ActionMenu {

    public ActionMenu(){

    }

    public void display(PeerConnection peercore, String user, UserProfile userProfile) throws NumberFormatException, Exception{

        String escolha, title, value, description;
        Number160 itemId;
        Random r = new Random();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Menu P2PBay");


        while(true) {
            System.out.println("Pressione a tecla pretendida:");
            System.out.println("1 - Create new Bid");
            System.out.println("2 - Search for bids");
            System.out.println("3 - My Purchase history");
            System.out.println("4 - My Bids");
            System.out.println("5 - Logout");
            escolha = br.readLine();
            if (escolha.equals("1")) {
                System.out.println("Item name:");
                title = br.readLine();
                System.out.println("Item value:");
                value = br.readLine();
                System.out.println("Item description:");
                description = br.readLine();
                if(title.isEmpty() || value.isEmpty() || description.isEmpty()){
                    System.out.println("*Invalid arguments - one is empty!");
                    continue;
                }
                itemId = new Number160(r);
                Item item = new Item(title, description, Double.parseDouble(value), user);
                userProfile.addMyBid(itemId.toString());
                peercore.store(itemId.toString() , item );
                peercore.store(user , userProfile );

            } else if (escolha.equals("5")){
                System.exit(0);
            }

            System.out.println("P2PBay - Coming Soon");
        }
    }


}
