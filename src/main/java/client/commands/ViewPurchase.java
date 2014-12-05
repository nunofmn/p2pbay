package client.commands;

import core.model.BidInfo;
import core.model.Item;
import core.model.UserProfile;
import core.network.PeerConnection;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.io.Resource;
import org.jboss.aesh.terminal.Shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CommandDefinition(name="purchase-history", description ="view purchases history")
public class ViewPurchase implements Command {
    private static final String USER = "user";
    private static final String ITEM = "item";
    private static final String BID = "bid";
    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    private PeerConnection peer;

    private UserProfile user;

    private String username;

    public ViewPurchase(PeerConnection peer, UserProfile user, String username) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();
        if(!user.getMyBidHistory().isEmpty()) {

            List<String> alreadyBought = new ArrayList<String>();
            BidInfo bid;
            for(int i = user.getMyBidHistory().size()-1; i >= 0 ; i--){
                try {
                    bid = user.getMyBidHistory().get(i);
                    Item item = (Item) peer.get(bid.getHashId(), ITEM);
                    if (item.getWinner().equals(username) && !alreadyBought.contains(bid.getHashId())) {
                        shell.out().println("You purchased " + bid.getTitle() + ", you spent " + bid.getValue() + " Euros.");
                        alreadyBought.add(bid.getHashId());
                    }
                } catch (Exception e) {
                }
            }

        }else{
            shell.out().println("You have no purchases.");

        }


        shell.out().println("View purchase command");

        return null;
    }
}
