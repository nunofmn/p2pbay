package client.commands;

import core.network.PeerConnection;
import gossip.GossipConnect;
import gossip.GossipMessage;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import java.io.IOException;
import java.util.List;

/**
 * Created by EngSoftware on 26-11-2014.
 */
@CommandDefinition(name="quit", description = "quit the program")
public class ExitCommand implements Command {

    private PeerConnection peer;
    private GossipConnect gossip;
    public ExitCommand(PeerConnection peer, GossipConnect gossip){
        this.peer = peer; this.gossip = gossip;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {

        List<Double> usersItems = peer.getNumUsersItems();
        gossip.sendMessage(new GossipMessage(gossip.getSumNodes()-1, gossip.getWeightNodes(),
                gossip.getSumUsers()-usersItems.get(0), gossip.getWeightUsers(),
                gossip.getSumItems()-usersItems.get(1) , gossip.getWeightItems(), gossip.getId()));

        peer.getPeer().shutdown();
        commandInvocation.getShell().out().println("Thank you for choosing p2pBay, see you soon!");
        commandInvocation.stop();
        System.exit(0);
        return CommandResult.SUCCESS;
    }
}
