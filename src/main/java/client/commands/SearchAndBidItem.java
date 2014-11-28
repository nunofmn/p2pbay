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
public class SearchAndBidItem implements Command {

    private static final String AND = "and";
    private static final String OR = "or";
    private static final String NOT = "not";
    private static final String USER = "user";
    private static final String ITEM = "item";
    private static final String BID = "bid";
    private static final String SEARCH = "search";

    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    private PeerConnection peer;

    private UserProfile user;

    private String username;

    public SearchAndBidItem(PeerConnection peer, UserProfile user, String username) {
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
        String bidvalue, option, bidoption;

        PromptForInputHelper helper = new PromptForInputHelper();
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
                        operandGetResult = peer.getIndexSearch(symbol, SEARCH);
                        if (operandGetResult == null ) {
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
                        NetworkContent getitem = peer.get(itemkey, ITEM);
                        if(getitem != null && getitem.contentType().equals("Item") && !((Item)getitem).isFinalized()) {
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


                    option = helper.promptForInput("Choose item to show: ", null, commandInvocation);

                    /*shell.out().println("Choose item to show: ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String option = br.readLine();*/


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
                    List<BidInfo> bids = peer.getBids(showitem.getUnHashedBidListId(), BID);
                    Double currentPrice = showitem.getValue();
                    if (bids != null && !bids.isEmpty()) {
                        for (BidInfo bid : bids) {
                            if(currentPrice < bid.getValue())
                                currentPrice = bid.getValue();
                            shell.out().println("User: " + bid.getHashId() + " bidded " + bid.getValue() + " Euros.");
                        }
                    } else {
                        System.out.println("No bids were made. Minimum bid is " + showitem.getValue() + " Euros.");
                    }

                    //bid on item
                    bidoption = helper.promptForInput("Bid on item? (y/n): ", null, commandInvocation);


                    if(bidoption.toString().equals("y")){
                        while(true) {
                            bidvalue = helper.promptForInput("Bid value (minimum: " + currentPrice + "): ", null, commandInvocation);

                            if (Double.parseDouble(bidvalue) > currentPrice) {

                                peer.addToList(showitem.getUnHashedBidListId(), new BidInfo(Double.parseDouble(bidvalue), this.username), BID);
                                this.user.addBid(new BidInfo(showitem.getTitle(), Double.parseDouble(bidvalue), showitem.getUnHashedKey()));

                                peer.store(this.username, this.user, USER);
                                shell.out().println("Bid successfully placed");
                                break;
                            } else {
                                shell.out().println("*Value is too low, you need more than " + currentPrice + " Euros");
                            }
                        }

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
            System.out.println("Didn't find word in DHT.");
        }

        return CommandResult.SUCCESS;
    }
}
