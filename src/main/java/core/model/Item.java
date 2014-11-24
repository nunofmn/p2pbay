package core.model;
import core.exception.*;

import java.util.ArrayList;
import  java.util.List;

public class Item extends NetworkContent {

    private String title;
    private String description;
    private String unHashedKey; //for when we add and save, as there are lots of items it's hark to keep track if not like this.
    private double value;
    private List<BidInfo> bidHistory;
    private boolean finalized = false;
    private String winner;
    private String identifier;
    private String owner;

    public Item(String title, String description, double initialValue, String owner, String unHashedKey) {
        super();
        this.title = title;
        this.description = description;
        this.value = initialValue;
        this.owner = owner;
        this.unHashedKey = unHashedKey;
        this.bidHistory = new ArrayList<BidInfo>();

    }



    public void addBid(BidInfo bid) throws ItemAlreadyFinalizedException, InvalidBidValueException {
        if(!finalized && bid.getValue() >= this.value ) {
            this.bidHistory.add(bid);
            this.value = bid.getValue();
        }
        else {
            if(finalized) {
                throw new ItemAlreadyFinalizedException(this.title);
            }else{
                throw new InvalidBidValueException(this.value, bid.getValue());
            }

        }

    }

    public void finalizeItem()throws NoBidsException{
        if(!bidHistory.isEmpty()) {
            this.finalized = true;
            winner = this.bidHistory.get(bidHistory.size() - 1).getHashId();
        }else{
            throw new NoBidsException(this.title);
        }
    }

    public void printBidHistoryInfo(){
        for(BidInfo bid : bidHistory) {
        }
        if(bidHistorySize() == 0){
            System.out.println("No bids were made. Minimum bid is " + this.getValue() + " Euros.");
        }
    }

    public List<BidInfo> getBidHistoryInfo() {
        return this.bidHistory;
    }

    public double getHighBidValue(){
        double minimum = this.getValue();
        for(BidInfo bid : bidHistory) {
            if(bid.getValue() > minimum)
                minimum = bid.getValue();
        }
        return minimum;
    }

    public int bidHistorySize(){
        return this.bidHistory.size();
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

    public String getWinner() throws ItemNotFinalizedException {
        if(finalized){
            return winner;
        }else {
            throw new ItemNotFinalizedException(this.title);
        }
    }

    public String getOwner() {
        return owner;
    }

    public String getUnHashedKey() {
        return unHashedKey;
    }

    @Override
    public String contentType(){
        return "Item";
    }

}

