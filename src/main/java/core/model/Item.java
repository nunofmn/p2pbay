package core.model;
import core.exception.*;

import java.util.ArrayList;
import  java.util.List;

public class Item extends NetworkContent {

    private String title;
    private String description;
    private String unHashedKey; //for when we add and save, as there are lots of items it's hark to keep track if not like this.
    private double value;
    private String unHashedBidListId;
    private boolean finalized = false;
    private String winner;
    private String identifier;
    private String owner;

    public Item(String title, String description, double initialValue, String owner, String unHashedKey, String unHashedBidListId) {
        super();
        this.title = title;
        this.description = description;
        this.value = initialValue;
        this.owner = owner;
        this.unHashedKey = unHashedKey;
        //this.bidHistory = new ArrayList<BidInfo>();
        this.unHashedBidListId = unHashedBidListId;

    }


    public boolean isFinalized(){
        return this.finalized;
    }


    public void finalizeItem(String winner)throws NoBidsException{
        this.winner = winner;
        this.finalized = true;
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

    public String getUnHashedBidListId(){
        return unHashedBidListId;
    }

    public String getUnHashedKey() {
        return unHashedKey;
    }

    @Override
    public String contentType(){
        return "Item";
    }

}

