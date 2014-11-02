package core.model;
import java.util.ArrayList;
import  java.util.List;

public class Item extends NetworkContent {

    private double value;
    private int id;
    private List<BidHistoryInfo> Bids;


    public Item(String description, int id, int value) {
        super();
        this.description = description;
        this.value = value;
        this.id = id;
        Bids = new ArrayList<BidHistoryInfo>();
    }

    public void addBid(BidHistoryInfo bid){
        Bids.add(bid);
    }

    public void printBidHistoryInfo(){
        for(BidHistoryInfo bid : Bids) {
            System.out.println();
        }
    }

    public String getDescription() {
        return description;
    }

    private String description;

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

}

