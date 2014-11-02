package core.model;
import java.util.ArrayList;
import  java.util.List;

public class Item extends NetworkContent {

    private String description;
    private double value;
    private int id;
    private List<BidHistoryInfo> Bids;
    private boolean finalizado = false;
    private String Vencedor;


    public Item(String description, int id, int value) {
        super();
        this.description = description;
        this.value = value;
        this.id = id;
        Bids = new ArrayList<BidHistoryInfo>();
    }

    public void addBid(BidHistoryInfo bid){
        Bids.add(bid);
        //this.Vencedor = Bids.get(Bids.size()).getUser
    }

    public void finalizarItem(){
        this.finalizado = true;

    }

    public void printBidHistoryInfo(){
        for(BidHistoryInfo bid : Bids) {
            System.out.println();
        }
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

}

