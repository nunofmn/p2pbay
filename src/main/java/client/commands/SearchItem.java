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

    private static final String AND = "and";
    private static final String OR = "or";
    private static final String NOT = "not";

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

    private boolean isOperand(String operand){
        return !operand.equals(AND)  && !operand.equals(OR) && !operand.equals(NOT);
    }

    private boolean isOperator(String operator){
        return operator.equals(AND) || operator.equals(OR) || operator.equals(NOT);
    }

    private List<String> computeOperator(String operator, List<String> operand1, List<String> operand2){
        List<String> result = new ArrayList<String>();
        if (operator.equals(OR)) { //union
            //Como e um AND um dos conjuntos pode estar vazio
            Set<String> set = new HashSet<String>();
            set.addAll(operand1);
            set.addAll(operand2);
            result = new ArrayList<String>(set);
        } else if (operator.equals(AND)) { //intersection
            for (String s : operand2) {
                if (operand1.contains(s)) {
                    result.add(s);
                }
            }
        } else if (operator.equals(NOT)) {
            result = operand2;
            result.removeAll(operand1);
        }
        return result;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();

        NetworkContent objectInDHT;

        try {
                List<String> operandGetResult = null;
                List<String> result = null;
                List<List<String>> stack = new ArrayList<List<String>>();
                List<String> operand1, operand2;
                String symbol;
                Item itemRealTime;

                for(int i= arguments.size() - 1; i >= 0; i--){
                    symbol = arguments.get(i).toString().toLowerCase();
                    if(isOperand(symbol)){
                        objectInDHT = peer.get(symbol);
                        if (objectInDHT != null && objectInDHT.contentType().equals("Search")) {
                            operandGetResult = ((core.model.SearchItem) objectInDHT).getAllItemsReferenced();
                        } else {
                            operandGetResult = new ArrayList<String>();
                        }
                        stack.add(operandGetResult);
                    } else if(isOperator(symbol)){
                        operand1 = stack.remove(stack.size()-1);
                        operand2 = stack.remove(stack.size()-1);
                        stack.add(computeOperator(symbol , operand1, operand2));

                    }
                }

                result = stack.remove(0);


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
                    itemRealTime = (Item)peer.get(showitem.getUnHashedKey()); //we retrieve the item again so it is current
                    shell.out().println("Title: " + itemRealTime.getTitle());
                    shell.out().println("Description: " + itemRealTime.getDescription());
                    shell.out().println("Bid History:");

                    //show item bids TODO - Refactor to another class
                    if (!itemRealTime.getBidHistoryInfo().isEmpty()) {
                        for (BidInfo bid : itemRealTime.getBidHistoryInfo()) {
                            shell.out().println("User: " + bid.getHashId() + " bidded " + bid.getValue() + " Euros.");
                        }
                    } else {
                        System.out.println("No bids were made. Minimum bid is " + itemRealTime.getValue() + " Euros.");
                    }

                    //bid on item
                    shell.out().println("Bid on item? (y/n)");
                    String bidoption = br.readLine();

                    if(bidoption.toString().equals("y")){
                        shell.out().println("Bid value (minimum: " + showitem.getHighBidValue() + "):");
                        String bidvalue = br.readLine();

                        itemRealTime = (Item)peer.get(showitem.getUnHashedKey());

                        //save bid
                        itemRealTime.addBid(new BidInfo(Double.parseDouble(bidvalue),this.username));
                        this.user.addMyPurchases(new BidInfo(itemRealTime.getTitle(), Double.parseDouble(bidvalue), showitem.getUnHashedKey()));
                        peer.store(itemRealTime.getUnHashedKey(), itemRealTime);
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



        }catch(ClassNotFoundException e) {
            System.out.println("Didnt find word in DHT.");
        }

        return CommandResult.SUCCESS;
    }
}
