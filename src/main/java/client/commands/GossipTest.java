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

@CommandDefinition(name="gossip", description ="test gossip protocol")
public class GossipTest implements Command {
    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    private PeerConnection peer;

    private UserProfile user;

    private String username;

    private GossipConnect gossip;

    public GossipTest(PeerConnection peer, UserProfile user, String username, GossipConnect gossip) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
        this.gossip = gossip;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();

        shell.out().println("Gossip test command!");

        gossip.sendMessage("Teste!");

        return null;
    }
}
