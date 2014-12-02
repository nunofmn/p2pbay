package client.commands;

import core.model.UserProfile;
import core.network.PeerConnection;
import gossip.GossipConnect;
import gossip.GossipMessage;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.io.Resource;
import org.jboss.aesh.terminal.Shell;

import java.io.IOException;
import java.util.List;

@CommandDefinition(name="npeers", description ="number of peers")
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

        shell.out().println("Number of peers: " + Math.round(gossip.getSum()/gossip.getWeight()));
        shell.out().println("Number of peers Sampaio: " + Math.ceil(gossip.getSum()/gossip.getWeight()));
        return null;
    }
}
