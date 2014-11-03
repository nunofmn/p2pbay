package core.model;

import java.io.Serializable;

/**
 * Created by EngSoftware on 01-11-2014.
 */
public class BidHistoryInfo implements Serializable {

    private String title;
    private String value;

    public BidHistoryInfo(String title, String value){
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
