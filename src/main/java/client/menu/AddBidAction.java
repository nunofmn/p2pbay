package client.menu;

import core.model.BidInfo;
import core.model.Item;
import core.model.UserProfile;
import core.network.PeerConnection;

/**
 * Created by EngSoftware on 20-11-2014.
 */
public class AddBidAction {
    private PeerConnection peercore;
    private Item item;
    private String user;
    private double value;
    private UserProfile userProfile;
    public AddBidAction(PeerConnection peercore, Item item, double value, String user, UserProfile userProfile){
        this.peercore = peercore;
        this.item = item;
        this.user = user;
        this.value = value;
        this.userProfile = userProfile;
    }

    public void execute() throws Exception{
        item.addBid(new BidInfo(value, user));
        this.userProfile.addMyPurchases(new BidInfo(item.getTitle(), value));
        peercore.store(item.getUnHashedKey(), item); //saves new bid
        peercore.store(user, userProfile);
        System.out.println("--Bid sucessfuly placed!--");
    }
}
