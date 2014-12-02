package client.commands;

import core.model.UserProfile;
import core.network.PeerConnection;
import gossip.GossipConnect;
import gossip.GossipMessage;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number480;
import net.tomp2p.storage.Data;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.io.Resource;
import org.jboss.aesh.terminal.Shell;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

@CommandDefinition(name="show", description ="test gossip protocol")
public class ShowStoredObj implements Command {
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

    private GossipConnect gossip;

    public ShowStoredObj(PeerConnection peer, UserProfile user, String username, GossipConnect gossip) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
        this.gossip = gossip;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();

        String userKey = Number160.createHash(USER).toString();
        String itemKey = Number160.createHash(ITEM).toString();
        int numUtilizadores = 0;
        int numItems = 0;

        shell.out().println("chave users - " + userKey);
        shell.out().println("chave item - " + itemKey);
        NavigableMap<Number480, Data> a =  peer.getPeer().getPeerBean().getStorage().map();
        int size = 0;
        for(Map.Entry<Number480, Data> entry : peer.getPeer().getPeerBean().getStorage().map().entrySet()){
            shell.out().println("--"+entry.getKey().toString());
            size++;
            if(entry.getKey().toString().contains(userKey)){
                numUtilizadores++;
            }
            if(entry.getKey().toString().contains(itemKey)){
                numItems++;
            }
        }

        shell.out().println("objectos guardados! - " + size);
        shell.out().println("Numero de utilizadores registados! - " + numUtilizadores);
        shell.out().println("Numero de items! - " + numItems);



        return null;
    }
}
