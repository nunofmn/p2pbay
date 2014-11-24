package gossip;

import java.io.Serializable;

public class GossipMessage implements Serializable {

    private Double sum;
    private Double weight;
    private int id;

    public GossipMessage(Double sum, Double weight, int id) {
        this.sum = sum;
        this.weight = weight;
        this.id = id;
    }

    public Double getSum() {
        return sum;
    }

    public Double getWeight() {
        return weight;
    }

    public int getId() {
        return id;
    }
}
