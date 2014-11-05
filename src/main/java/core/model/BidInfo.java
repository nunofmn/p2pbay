package core.model;

import java.io.Serializable;

/**
 * Created by EngSoftware on 01-11-2014.
 */
public class BidInfo implements Serializable {

    private String title;
    private double value;
    private String user;

    public BidInfo(String title, double value){
        this.title = title;
        this.value = value;
    }

    public BidInfo(String title, double value, String user){
        this.title = title;
        this.value = value;
        this.user = user;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
