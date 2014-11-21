package client.commands;

import core.model.Item;
import core.model.NetworkContent;
import core.model.UserProfile;
import core.network.PeerConnection;
import net.tomp2p.peers.Number160;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.io.Resource;
import org.jboss.aesh.terminal.Shell;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@CommandDefinition(name="sell", description ="sell an item")
public class SellItem implements Command {
    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    private PeerConnection peer;

    private UserProfile user;

    private String username;

    public SellItem(PeerConnection peer, UserProfile user, String username) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {

        this.shell = commandInvocation.getShell();

        shell.out().println("Sell Item command");

        Number160 itemId;
        NetworkContent objectInDHT;
        Random r = new Random();
        itemId = new Number160(r);

        String title = arguments.get(0).toString();
        String description = arguments.get(1).toString();
        Double minimumbid = Double.parseDouble(arguments.get(2).toString());

        shell.out().println("Arguments: " + title + " " + description + " " + minimumbid);

        // arguments: title, description, minimumbid, username
        Item item = new Item(title, description, minimumbid, username, itemId.toString());
        user.addMyBid(itemId.toString());
        peer.store(itemId.toString() , item );
        peer.store(username , user);

        //procuramos na DHT pela palavra e adicionamos o id item
        try {
            for (String s : title.split(" ")) {
                objectInDHT = peer.get(s);
                if (objectInDHT == null) {
                    objectInDHT = new core.model.SearchItem();
                }
                ((core.model.SearchItem) objectInDHT).addReferenceItem(itemId.toString());
                peer.store(s, objectInDHT);
            }
        }catch(ClassNotFoundException e) {
            shell.out().println("Item not found.");

        }

        shell.out().println("Item added to P2PBay.");

        return CommandResult.SUCCESS;
    }

}
