package core.model;

import java.util.ArrayList;
import  java.util.List;

public class UserProfile extends NetworkContent {
    private String password;
    private List<String> myItems;
    private List<BidInfo> myBidHistory;

    public UserProfile(String password){
        super();
        this.password = password;
        this.myItems = new ArrayList<String>();
        this.myBidHistory = new ArrayList<BidInfo>();
    }

    public Boolean login(String password){
        return this.password.equals(password);
    }

    @Override
    public String contentType(){return "User";}

    public void addItem(String id){ myItems.add(id);}

    public void addBid(BidInfo item){this.myBidHistory.add(item);}

    public List<BidInfo> getMyBidHistory(){return this.myBidHistory;}

    public List<String> getMyItems(){return this.myItems;}


}
