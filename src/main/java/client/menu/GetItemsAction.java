package client.menu;

import core.model.NetworkContent;
import core.network.PeerConnection;
import core.model.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EngSoftware on 20-11-2014.
 */
public class GetItemsAction {


    private PeerConnection peercore;
    private List<String> hashList;
    private List<Item> result;

    public GetItemsAction(PeerConnection peercore) {
        this.peercore = peercore;
        this.hashList = null;
    }

    public void addList(List<String> results){
        this.hashList = results;
    }


    public void execute() throws Exception{
       /* NetworkContent tmp;
        if (this.hashList != null && !this.hashList.isEmpty()) {
            this.result = new ArrayList<Item>();
            for (String s : this.hashList) {
                tmp = this.peercore.get(s);
                if (tmp != null && tmp.contentType().equals("Item"))
                    this.result.add((Item)tmp);
            }
        }*/
    }

    public List<Item> getResult(){
        return this.result;
    }

}
