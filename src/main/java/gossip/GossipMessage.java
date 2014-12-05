package gossip;

import java.io.Serializable;

public class GossipMessage implements Serializable {

    private Double sumNodes;
    private Double weightNodes;
    private Double sumUsers;
    private Double weightUsers;
    private Double sumItems;
    private Double weightItems;
    private int id;

    public GossipMessage(Double sumNodes, Double weightNodes,
                         Double sumUsers, Double weightUsers,
                         Double sumItems, Double weightItems, int id) {
        this.sumNodes = sumNodes;
        this.weightNodes = weightNodes;

        this.setSumUsers(sumUsers);
        this.setWeightUsers(weightUsers);

        this.setSumItems(sumItems);
        this.setWeightItems(weightItems);
        this.id = id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Double getSumNodes() {
        return sumNodes;
    }

    public Double getWeightNodes() {
        return weightNodes;
    }

    public int getId() {
        return id;
    }

    public void setSumNodes(Double sumNodes) {
        this.sumNodes = sumNodes;
    }

    public void setWeightNodes(Double weightNodes) {
        this.weightNodes = weightNodes;
    }

    public Double getSumUsers() {
        return sumUsers;
    }

    public void setSumUsers(Double sumUsers) {
        this.sumUsers = sumUsers;
    }

    public Double getWeightUsers() {
        return weightUsers;
    }

    public void setWeightUsers(Double weightUsers) {
        this.weightUsers = weightUsers;
    }

    public Double getSumItems() {
        return sumItems;
    }

    public void setSumItems(Double sumItems) {
        this.sumItems = sumItems;
    }

    public Double getWeightItems() {
        return weightItems;
    }

    public void setWeightItems(Double weightItems) {
        this.weightItems = weightItems;
    }
}
