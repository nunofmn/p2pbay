package core.model;

import java.util.ArrayList;
import  java.util.List;

public class UserProfile extends NetworkContent {
    private String password;
    private List<String> myBids;
    private List<BidInfo> myPurchases;

    public UserProfile(String password){
        super();
        this.password = password;
        this.myBids = new ArrayList<String>();
        this.myPurchases = new ArrayList<BidInfo>();
    }

    public Boolean login(String password){
        return this.password.equals(password);
    }

    @Override
    public String contentType(){
        return "User";
    }

    public void addMyBid(String id){
        myBids.add(id);
    }

}
