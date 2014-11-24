package core.model;

import java.io.Serializable;

/**
 * Created by EngSoftware on 01-11-2014.
 */
public class BidInfo implements Serializable {

    private String title;
    private double value;
    private String hashId;

    public BidInfo(String title, double value, String hashId){
        this.title = title;
        this.value = value;
        this.hashId = hashId;
    }

    public BidInfo(double value, String hashId){
        this.value = value;
        this.hashId = hashId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }
}
