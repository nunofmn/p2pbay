package core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EngSoftware on 08-11-2014.
 */
public class SearchItem  extends NetworkContent {

    private List<String> itemIds;

    public SearchItem(){
        this.itemIds = new ArrayList<String>();
    }

    public void addReferenceItem(String id){
        this.itemIds.add(id);
    }

    public List<String> getAllItemsReferenced(){
        return this.itemIds;
    }

    @Override
    public String contentType(){
        return "Search";
    }

}
