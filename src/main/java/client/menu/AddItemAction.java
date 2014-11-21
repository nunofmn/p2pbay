package client.menu;

import core.model.Item;
import core.model.NetworkContent;
import core.model.SearchItem;
import core.model.UserProfile;
import core.network.PeerConnection;
import net.tomp2p.peers.Number160;

import java.util.Random;

public class AddItemAction {

    private PeerConnection peercore;
    String title, value, description, user;
    UserProfile userProfile;

    public AddItemAction(PeerConnection peercore, String title, String value , String description , String user, UserProfile userProfile) {
        this.peercore = peercore;
        this.userProfile = userProfile;
        this.user = user;
        this.title = title;
        this.value = value;
        this.description = description;

    }

    public void execute() throws Exception{
        Number160 itemId;
        NetworkContent objectInDHT;
        Random r = new Random();
        itemId = new Number160(r);
        Item item = new Item(title, description, Double.parseDouble(value), user, itemId.toString());
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
    }
}
