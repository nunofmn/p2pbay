package client.menu;

import core.model.NetworkContent;
import core.model.SearchItem;
import core.network.PeerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by EngSoftware on 20-11-2014.
 */
public class SearchAction {

    private PeerConnection peercore;
    private List<String> results;

    public SearchAction(PeerConnection peercore) {
        this.peercore = peercore;
        this.results = new ArrayList<String>();
    }



    public void execute() throws Exception {

        NetworkContent objectInDHT;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type the item name you want to see: (ex: AND ipod 32gb)");
        String title = br.readLine();
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
                this.results = result;
                System.out.println("We found " + result.size() + " results:");
            }

        }else{
            System.out.println("Too many words... please write only two words and an boolean operation");
        }
    }

    public List<String> getResults(){
        return this.results;
    }



}
