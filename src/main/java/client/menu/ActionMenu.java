package client.menu;

import core.model.Item;
import core.model.NetworkContent;
import core.model.SearchItem;
import core.model.UserProfile;
import core.network.PeerConnection;
import net.tomp2p.peers.Number160;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

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
        NetworkContent objectInDHT;

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
                title = title.toLowerCase();
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
                //procuramos na DHT pela palavra e adicionamos o id item
                for(String s: title.split(" ")){
                    objectInDHT = peercore.get(s);
                    if(objectInDHT == null){
                        objectInDHT = new SearchItem();
                    }
                    ((SearchItem)objectInDHT).addReferenceItem(itemId.toString());
                    peercore.store(s , objectInDHT );
                }





            }else if (escolha.equals("2")) {
                SearchAction search = new SearchAction(peercore);
                search.execute();

                GetItemsAction getItems = new GetItemsAction(peercore);
                getItems.addList(search.getResults());
                getItems.execute();

                while(true) {
                    if (getItems.getResult() != null) {
                        System.out.println("0 - Back!");
                        int i = 1;
                        for (Item n : getItems.getResult()) {
                            System.out.println(i + " - " + n.getTitle());
                            i++;
                        }
                        System.out.println("Press a key:");
                        escolha = br.readLine();
                        if(escolha.equals("0")){
                            break;
                        }else {
                            i = -1;
                            try{
                                i = Integer.parseInt(escolha);
                            }catch (Exception e){
                                System.out.println("Pressed key not valid!");
                            }

                            if(i > 0 && i <= getItems.getResult().size()){
                                i = i-1; // como o 0 serve para sair
                                Item bid = getItems.getResult().get(i);
                                System.out.println("Title: " + bid.getTitle());
                                System.out.println("Description: " + bid.getDescription());
                                System.out.println("Bid History: ");
                                bid.printBidHistoryInfo();

                                System.out.println("0 - back to main menu" );
                                System.out.println("1 - View other item" );
                                escolha = br.readLine();
                                if(escolha.equals("0")){
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("No search results, try a different query.");
                        break;
                    }

                }


            }
            else if (escolha.equals("5")){
                System.exit(0);
            }

            System.out.println("P2PBay - Coming Soon");
        }
    }


}
