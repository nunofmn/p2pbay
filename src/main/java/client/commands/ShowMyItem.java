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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@CommandDefinition(name="show-my-items", description ="Show my items information")
public class ShowMyItem implements Command{

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

    public ShowMyItem(PeerConnection peer, UserProfile user, String username) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();

        PromptForInputHelper helper = new PromptForInputHelper();
        List<Item> items = new ArrayList<Item>();
        List<String> itemsIDs = user.getMyItems();
        NetworkContent dhtObject;

        if(itemsIDs.isEmpty()){
            shell.out().println("You have no items for sale...");
            return CommandResult.SUCCESS;
        }

        for(String itemId : itemsIDs){

            try {
                dhtObject = peer.get(itemId, ITEM);

                if(dhtObject != null && dhtObject.contentType().equals("Item"))
                    items.add((Item)dhtObject);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        shell.out().println();
        shell.out().println("#########Your Items############");
        int i = 1;
        for (Item item : items){
            shell.out().print("Item " + i +": Title: " + item.getTitle());
            if(item.isFinalized())
                shell.out().println(" (Finalized)");
            else
                shell.out().println();
            i++;
        }
        shell.out().println("###############################");

        while(true) {
            shell.out().println("\nPress 0 to exit.");
            shell.out().println("Press number of item to show item bid history.");
            shell.out().println("Press f(number of item) to finalize item.\n");

            String option = helper.promptForInput(": ", null, commandInvocation);
            //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //String option = br.readLine();

            if (option.equals("0")) {//sair
                return CommandResult.SUCCESS;

            } else if (option.startsWith("f")) {//finalizar um item

                int index = Integer.parseInt((String) option.subSequence(1, option.length()));
                Item item = items.get(index-1);
                String unHashedBidListId = item.getUnHashedBidListId();
                List<BidInfo> myItemBids = null;

                try {
                    myItemBids = peer.getBids(unHashedBidListId, BID);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (myItemBids == null) {
                    shell.out().println("Item " + item.getTitle() + " has 0 bids, can not be finalized");
                    continue;
                }

                BidInfo winingBid = myItemBids.get(0);
                for (BidInfo bid : myItemBids) {
                    if(bid.getValue() > winingBid.getValue()){
                        winingBid = bid;
                    }
                }

                item.finalizeItem(winingBid.getHashId());//aqui HashId tem o nome do utilizador
                peer.store(item.getUnHashedKey() , item, ITEM);
                shell.out().println("Item " + item.getTitle() + " has been successful finalized.\n");

            } else {

                int index = Integer.parseInt((String) option);
                Item item = items.get(index-1);
                String unHashedBidListId = item.getUnHashedBidListId();
                List<BidInfo> myItemBids = null;

                try {
                    myItemBids = peer.getBids(unHashedBidListId, BID);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (myItemBids == null) {
                    shell.out().println("Item " + item.getTitle() + " has 0 bids.\n");
                    continue;
                }

                for (BidInfo bid : myItemBids) {
                    shell.out().println("User " + bid.getHashId() + " placed a bid of " + bid.getValue() );
                }
            }
        }

        //return null;
    }
}
