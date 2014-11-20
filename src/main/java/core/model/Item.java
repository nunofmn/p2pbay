package core.model;
import core.exception.*;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import  java.util.List;
import java.util.Random;

public class Item extends NetworkContent {

    private String title;
    private String description;
    private double value;
    private List<BidInfo> bidHistory;
    private boolean finalized = false;
    private String winner;
    private String identifier;
    private String owner;

    public Item(String title, String description, double initialValue, String owner) {
        super();
        this.title = title;
        this.description = description;
        this.value = initialValue;
        this.owner = owner;
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
            winner = this.bidHistory.get(bidHistory.size() - 1).getUser();
        }else{
            throw new NoBidsException(this.title);
        }
    }

    public void printBidHistoryInfo(){
        for(BidInfo bid : bidHistory) {
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

    @Override
    public String contentType(){
        return "Item";
    }

}

