package client;

import client.commands.*;
import client.menu.LoginMenu;
import core.model.UserProfile;
import core.network.PeerConnection;
import gossip.GossipConnect;
import org.apache.logging.log4j.LogManager;
import org.jboss.aesh.console.AeshConsole;
import org.jboss.aesh.console.AeshConsoleBuilder;
import org.jboss.aesh.console.Prompt;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.registry.AeshCommandRegistryBuilder;
import org.jboss.aesh.console.command.registry.CommandRegistry;
import org.jboss.aesh.console.settings.Settings;
import org.jboss.aesh.console.settings.SettingsBuilder;
import org.jboss.aesh.terminal.Color;
import org.jboss.aesh.terminal.TerminalColor;
import org.jboss.aesh.terminal.TerminalString;
import org.apache.logging.log4j.Logger;

public class ShellMain {

    private static PeerConnection peer;
    private static UserProfile user;
    private static String username;
    final static Logger logger = LogManager.getLogger(ShellMain.class);

    public static void main(String[] args) throws Exception {

        if (args.length == 3) {
            peer = new PeerConnection(args[0], args[1], args[2]);
        }
        if (args.length == 1) {
            peer = new PeerConnection(args[0]);
        }

        //Login
        LoginMenu login = new LoginMenu();
        login.display(peer);
        user = login.getUserProfile();
        username = login.getUsername();

        GossipConnect gossip = new GossipConnect(peer.getPeer());

        SettingsBuilder builder = new SettingsBuilder().logging(true);
        builder.enableMan(false)
                .readInputrc(false);

        Settings settings = builder.create();

        //User interface
        Command gossipTest = new GossipTest(peer,user,username,gossip);
        Command numberOfPeers = new NumberOfPeers(peer,user,username,gossip);
        Command sellItem = new SellItem(peer,user,username);
        Command acceptBid = new AcceptBid(peer,user,username);
        Command searchItem = new SearchItem(peer,user,username);
        Command viewItem = new ViewItem(peer,user,username);
        Command viewPurchase = new ViewPurchase(peer,user,username);
        Command biddingHistory = new BiddingHistory(peer,user,username);


        CommandRegistry registry = new AeshCommandRegistryBuilder()
                .command(gossipTest)
                .command(numberOfPeers)
                .command(sellItem)
                .command(acceptBid)
                .command(searchItem)
                .command(viewItem)
                .command(viewPurchase)
                .command(biddingHistory)
                .create();

        AeshConsole aeshConsole = new AeshConsoleBuilder()
                .commandRegistry(registry)
                .settings(settings)
                .prompt(new Prompt(new TerminalString("p2pbay> ",
                        new TerminalColor(Color.GREEN, Color.DEFAULT, Color.Intensity.BRIGHT))))
                .create();

        aeshConsole.start();
    }

}
