package core.model;
import core.exception.*;
import java.util.ArrayList;
import  java.util.List;

public class Item extends NetworkContent {

    private String title;
    private String description;
    private double value;
    private List<BidHistoryInfo> BidHistory;
    private boolean finalized = false;
    private String winner;

    public Item(String title, String description, int initialValue) {
        super();
        this.title = title;
        this.description = description;
        this.value = initialValue;
        this.BidHistory = new ArrayList<BidHistoryInfo>();
    }

    public void addBid(BidHistoryInfo bid) throws ItemFinalized, InvalidBidValue{
        if(!finalized && bid.getValue() >= this.value ) {
            this.BidHistory.add(bid);
            this.value = bid.getValue();
        }
        else {
            if(finalized) {
                throw new ItemFinalized(this.title);
            }else{
                throw new InvalidBidValue(this.value, bid.getValue());
            }

        }

    }

    public void finalizeItem(){
        this.finalized = true;
        winner = this.BidHistory.get(BidHistory.size() -1).getUser();
    }

    public void printBidHistoryInfo(){
        for(BidHistoryInfo bid : BidHistory) {
            System.out.println();
        }
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public double getValue() {
        return value;
    }

    public String getWinner() throws ItemNotFinalized{
        if(finalized){
            return winner;
        }else {
            throw new ItemNotFinalized(this.title);
        }
    }

}

