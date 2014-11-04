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
    private List<BidHistoryInfo> bidHistory;
    private boolean finalized = false;
    private String winner;
    private String identifier;

    public Item(String title, String description, int initialValue) {
        super();
        this.title = title;
        this.description = description;
        this.value = initialValue;
        this.bidHistory = new ArrayList<BidHistoryInfo>();
        generateIdentifier();
    }

    private void generateIdentifier(){
        //System.out.println(text);
        Random rand = new Random(System.currentTimeMillis());
        Double randDoubleId = rand.nextDouble();
        String randTextId = randDoubleId.toString();
        //System.out.println(randTextId);
        MessageDigest digest = null;
        byte[] hash=null;

        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(randTextId.getBytes("UTF-8"));
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
            this.bidHistory.add(bid);
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

    public void finalizeItem()throws NoBidsException{
        if(!bidHistory.isEmpty()) {
            this.finalized = true;
            winner = this.bidHistory.get(bidHistory.size() - 1).getUser();
        }else{
            throw new NoBidsException(this.title);
        }
    }

    public void printBidHistoryInfo(){
        for(BidHistoryInfo bid : bidHistory) {
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

