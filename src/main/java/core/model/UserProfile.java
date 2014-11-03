package core.model;

import java.util.ArrayList;
import  java.util.List;

public class UserProfile extends NetworkContent {
    private String password;
    private List<BidHistoryInfo> myBids;
    private List<BidHistoryInfo> myPurchases;

    public UserProfile(String password){
        super();
        this.password = password;
        this.myBids = new ArrayList<BidHistoryInfo>();
        this.myPurchases = new ArrayList<BidHistoryInfo>();
    }

    public Boolean login(String password){
        return this.password.equals(password);
    }

    @Override
    public String contentType(){
        return "User";
    }

}
