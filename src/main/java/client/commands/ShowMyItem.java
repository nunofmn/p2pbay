package client.commands;

import com.sun.corba.se.impl.transport.ByteBufferPoolImpl;
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

@CommandDefinition(name="show-my-items", description ="fhjgh gjng")
public class ShowMyItem implements Command{

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

/*
        this.shell = commandInvocation.getShell();

        List<Item> items = new ArrayList<Item>();
        NetworkContent dhtObject;

        List<String> itemsIDs = user.getMyItems();
        for(String item : itemsIDs){

            try {
                dhtObject = peer.get(item);

                if(dhtObject != null && dhtObject.contentType().equals("Item"))
                    items.add((Item)dhtObject);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        int i = 1;
        for (Item item : items){
            shell.out().println("Item " + i +": Title: " + item.getTitle() +
                    ", Description: " + item.getDescription() +
                    ", Value: " + item.getValue());
            i++;
        }

        shell.out().println("Press 0 to exit.");
        shell.out().println("Press number of item to show item bid history.");
        shell.out().println("Press f(number of item) to finalize item.");

        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //String option = br.readLine();

        String option = "1";
        shell.out().println(option);

        if (option.equals("0")) {//sair
            return CommandResult.SUCCESS;

        }else if (option.charAt(0) == 'f') {//finalizar um item

            int index = Integer.parseInt((String)  option.subSequence(1, option.length()));
            shell.out().println("f(Index):f" + index);
            Item item = items.get(index);
            item.finalizeItem();

            peer.store(item.getUnHashedKey() , item );
            peer.store(username , user);
            shell.out().println(index);

            shell.out().println("Item " + item.getTitle() + " Finalizado");
        }else{ //mostar bid history de um item

            int index = Integer.parseInt(option);
            shell.out().println("(Index):f" + index);
            Item item = items.get(index);
            List<BidInfo> bids = item.getBidHistoryInfo();

            for(BidInfo bid : bids){
                shell.out().println("Bid by: " + bid.getUsername() + ", with value of: " + bid.getValue());
            }


        }

        shell.out().println("Accept bid command");

*/
        return null;
    }
}
