package core.model;
import java.util.ArrayList;
import  java.util.List;

public class Item extends NetworkContent {

    private String titulo;
    private String description;
    private double value;
    private int id;
    private List<BidHistoryInfo> Bids;
    private boolean finalizado = false;
    private String Vencedor;


    public Item(String titulo, String description, int id, int value) {
        super();
        this.description = description;
        this.value = value;
        this.id = id;
        this.Bids = new ArrayList<BidHistoryInfo>();
    }

    public void addBid(BidHistoryInfo bid){
        Bids.add(bid);
        //this.Vencedor = bid.getUser();
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

    public String getTitulo() {
        return titulo;
    }

    public double getValue() {
        return value;
    }

    public String getVencedor() throws ItemNotFinalized{
        if(finalizado){
            return this.Bids.get(Bids.size() -1).getUser();
        }
        throws new ItemNotFinalized(this.titulo);
    }

}

