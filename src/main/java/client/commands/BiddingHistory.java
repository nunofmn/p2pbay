package client.commands;

import core.model.BidInfo;
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
import java.util.List;

@CommandDefinition(name="bids-history", description ="see user bidding history")
public class BiddingHistory implements Command{

    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    private PeerConnection peer;

    private UserProfile user;

    private String username;

    public BiddingHistory(PeerConnection peer, UserProfile user, String username) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();

        for(BidInfo bid : user.getMyBidHistory()){
            shell.out().println("You bidded " + bid.getValue() + " Euros on item: " + bid.getTitle());
        }



        //shell.out().println("Bidding history command");

        return null;
    }
}
