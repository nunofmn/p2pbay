package client.commands;

import core.model.UserProfile;
import core.network.PeerConnection;
import gossip.GossipConnect;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.io.Resource;
import org.jboss.aesh.terminal.Shell;

import java.io.IOException;
import java.util.List;

@CommandDefinition(name="statistics", description ="number of peers, users and items")
public class NumberOfPeers implements Command {
    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    private PeerConnection peer;

    private UserProfile user;

    private String username;

    private GossipConnect gossip;

    public NumberOfPeers(PeerConnection peer, UserProfile user, String username, GossipConnect gossip) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
        this.gossip = gossip;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();

        double numPeers = gossip.getSumNodes()/gossip.getWeightNodes();

        if(numPeers > 6)
            numPeers = 6.0;
        double numUsers = (gossip.getSumUsers() / gossip.getWeightUsers())/ numPeers;
        double numItems = (gossip.getSumItems() / gossip.getWeightItems())/ numPeers;


        shell.out().println("Number of peers: ");
        shell.out().println("Rounded:" + Math.round(gossip.getSumNodes()/gossip.getWeightNodes()));
        shell.out().println("Correct value:" + gossip.getSumNodes()/gossip.getWeightNodes());

        shell.out().println("Number of users: ");
        shell.out().println("Rounded:" + Math.round(numUsers));
        shell.out().println("Correct value:" + numUsers);

        shell.out().println("Number of items: ");
        shell.out().println("Rounded:" + Math.round(numItems));
        shell.out().println("Correct value:" + numItems);

        return null;
    }
}
