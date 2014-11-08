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
                System.out.println("Type the item name you want to see: (ex: AND ipod 32gb)");
                title = br.readLine();
                title = title.toLowerCase();
                String[] words = title.split(" ");

                if(words.length == 3){
                    List<String> resultsWord1 = null;
                    List<String> resultsWord2 = null;
                    List<String> result = null;
                    objectInDHT = peercore.get(words[1]);
                    if(objectInDHT != null && objectInDHT.contentType().equals("Search")){
                        resultsWord1 = ((SearchItem)objectInDHT).getAllItemsReferenced();
                    }else{
                        resultsWord1 = new ArrayList<String>();
                    }
                    objectInDHT = peercore.get(words[2]);
                    if(objectInDHT != null && objectInDHT.contentType().equals("Search")){
                        resultsWord2 = ((SearchItem)objectInDHT).getAllItemsReferenced();
                    }else{
                        resultsWord2 = new ArrayList<String>();
                    }
                    if(words[0].toLowerCase().equals("or")){ //union
                        //Como e um AND um dos conjuntos pode estar vazio
                        Set<String> set = new HashSet<String>();
                        set.addAll(resultsWord1);
                        set.addAll(resultsWord2);
                        result = new ArrayList<String>(set);

                    }else if(words[0].toLowerCase().equals("and")){ //intersection
                        result = new ArrayList<String>();
                        for(String s : resultsWord1){
                            if(resultsWord2.contains(s)){
                                result.add(s);
                            }
                        }
                    } else if(words[0].toLowerCase().equals("not")){

                    }


                    //mostrar resultados
                    if(result != null) {
                        System.out.println("We found " + result.size() + " results:");
                        for (String s : result) {
                            System.out.println("Id: " + s);

                        }
                        System.out.println("Temos de de po-lo a ir buscar os titulos à DHT, talvez");
                    }

                }else{
                    System.out.println("Too many words... please write only two words and an boolean operation");
                }



            }
            else if (escolha.equals("5")){
                System.exit(0);
            }

            System.out.println("P2PBay - Coming Soon");
        }
    }


}
