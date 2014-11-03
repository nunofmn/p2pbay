package core.model;
import core.exception.*;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import  java.util.List;

public class Item extends NetworkContent {

    private String title;
    private String description;
    private double value;
    private List<BidHistoryInfo> BidHistory;
    private boolean finalized = false;
    private String winner;
    private Date creationDate;
    private String identifier;

    public Item(String title, String description, int initialValue) {
        super();
        this.title = title;
        this.description = description;
        this.value = initialValue;
        this.BidHistory = new ArrayList<BidHistoryInfo>();
        this.creationDate = new Date();
        generateIdentifier();
    }

    private void generateIdentifier(){

        String text = creationDate.toString().concat(title);
        //System.out.println(text);
        MessageDigest digest = null;
        byte[] hash=null;

        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(text.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.identifier =  DatatypeConverter.printHexBinary(hash);
        //System.out.println(this.identifier);


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

