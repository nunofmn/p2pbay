package client.commands;

import core.model.BidInfo;
import core.model.Item;
import core.model.NetworkContent;
import core.model.UserProfile;
import core.network.PeerConnection;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.io.Resource;
import org.jboss.aesh.terminal.Shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CommandDefinition(name="search", description ="search for an item")
public class SearchItem implements Command {
    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    private PeerConnection peer;

    private UserProfile user;

    private String username;

    public SearchItem(PeerConnection peer, UserProfile user, String username) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();

        NetworkContent objectInDHT;

        try {

            if (arguments.size() == 3) {
                List<String> resultsWord1 = null;
                List<String> resultsWord2 = null;
                List<String> result = null;
                objectInDHT = peer.get(arguments.get(1).toString());
                if (objectInDHT != null && objectInDHT.contentType().equals("Search")) {
                    resultsWord1 = ((core.model.SearchItem) objectInDHT).getAllItemsReferenced();
                } else {
                    resultsWord1 = new ArrayList<String>();
                }
                objectInDHT = peer.get(arguments.get(2).toString());
                if (objectInDHT != null && objectInDHT.contentType().equals("Search")) {
                    resultsWord2 = ((core.model.SearchItem) objectInDHT).getAllItemsReferenced();
                } else {
                    resultsWord2 = new ArrayList<String>();
                }
                if (arguments.get(0).toString().toLowerCase().equals("or")) { //union
                    //Como e um AND um dos conjuntos pode estar vazio
                    Set<String> set = new HashSet<String>();
                    set.addAll(resultsWord1);
                    set.addAll(resultsWord2);
                } else if (arguments.get(0).toString().toLowerCase().equals("and")) { //intersection
                    result = new ArrayList<String>();
                    for (String s : resultsWord1) {
                        if (resultsWord2.contains(s)) {
                            result.add(s);
                        }
                    }
                } else if (arguments.get(0).toString().toLowerCase().equals("not")) {
                    result = resultsWord1;
                    result.removeAll(resultsWord2);
                }

                // get items from results TODO - Refactor to another class
                List<Item> items = new ArrayList<Item>();

                if(result != null && !result.isEmpty()) {

                    for(String itemkey : result) {
                        NetworkContent getitem = peer.get(itemkey);
                        if(getitem != null && getitem.contentType().equals("Item")) {
                            items.add((Item)getitem);
                        }
                    }
                }


                //show option TODO - Refactor to another class
                if(!items.isEmpty()) {
                    //show results TODO - Refactor to another class
                    int count = 1;
                    shell.out().println("0 - Exit Search");
                    for(Item item : items) {
                        shell.out().println(count + " - " + item.getTitle());
                        count++;
                    }

                    shell.out().println("Choose item to show: ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String option = br.readLine();


                    try {
                        if (Integer.parseInt(option) == 0) {
                            return CommandResult.FAILURE;
                        }
                    }catch(NumberFormatException e) {
                        shell.out().println("Invalid option");
                        return CommandResult.FAILURE;
                    }

                    //show item TODO - Refactor to another class
                    Item showitem = items.get(Integer.parseInt(option)-1);
                    shell.out().println("Title: " + showitem.getTitle());
                    shell.out().println("Description: " + showitem.getDescription());
                    shell.out().println("Bid History:");

                    //show item bids TODO - Refactor to another class
                    if (!showitem.getBidHistoryInfo().isEmpty()) {
                        for (BidInfo bid : showitem.getBidHistoryInfo()) {
                            shell.out().println("User: " + bid.getHashId() + " bidded " + bid.getValue() + " Euros.");
                        }
                    } else {
                        System.out.println("No bids were made. Minimum bid is " + showitem.getValue() + " Euros.");
                    }

                    //bid on item
                    shell.out().println("Bid on item? (y/n)");
                    String bidoption = br.readLine();

                    if(bidoption.toString().equals("y")){
                        shell.out().println("Bid value (minimum: " + showitem.getHighBidValue() + "):");
                        String bidvalue = br.readLine();

                        //save bid
                        showitem.addBid(new BidInfo(Double.parseDouble(bidvalue),this.username));
                        this.user.addMyPurchases(new BidInfo(showitem.getTitle(), Double.parseDouble(bidvalue), showitem.getUnHashedKey()));
                        peer.store(showitem.getUnHashedKey(), showitem);
                        peer.store(this.username, this.user);
                        shell.out().println("Bid successfully placed");

                    }else if(bidoption.toString().equals("n")) {
                        return CommandResult.SUCCESS;
                    }else{
                        shell.out().println("Invalid option");
                        return CommandResult.FAILURE;
                    }

                }else{
                    shell.out().println("No items to show");
                }

            } else {
                System.out.println("Too many words... please write only two words and an boolean operation");
            }

        }catch(ClassNotFoundException e) {
            System.out.println("Didnt find word in DHT.");
        }

        return CommandResult.SUCCESS;
    }
}
