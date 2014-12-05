package client;

import client.commands.*;
import client.menu.LoginMenu;
import core.model.UserProfile;
import core.network.PeerConnection;
import gossip.GossipConnect;
import gossip.GossipMessage;
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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShellMain {

    private static PeerConnection peer;
    private static UserProfile user;
    private static String username;

    public static void main(String[] args) throws Exception {

        if (args.length == 3) {
            peer = new PeerConnection(args[0], args[1], args[2]);
        }
        if (args.length == 1) {
            peer = new PeerConnection(args[0]);
        }

        username = "";


        final GossipConnect gossip = new GossipConnect(peer, username, false);

        // gossip protocol thread executor
        ScheduledExecutorService gossipexecutor = Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = new Runnable() {
            public void run() {
                gossip.sendMessage(new GossipMessage(gossip.getSumNodes()/2, gossip.getWeightNodes()/2,
                        gossip.getSumUsers()/2, gossip.getWeightUsers()/2,
                        gossip.getSumItems()/2 , gossip.getWeightItems()/2, gossip.getId()));
            }
        };

        gossipexecutor.scheduleAtFixedRate(periodicTask, 0, 5, TimeUnit.SECONDS);

        //Login
        LoginMenu login = new LoginMenu();
        login.display(peer);
        user = login.getUserProfile();
        username = login.getUsername();
        gossip.setUsername(username);
        if(username.equals("Admin")){
            gossip.startGossipAdmin();
        }

        SettingsBuilder builder = new SettingsBuilder().logging(true);
        builder.enableMan(false)
                .readInputrc(false);

        Settings settings = builder.create();

        //User interface
        Command numberOfPeers = new NumberOfPeers(peer,user,username,gossip);
        Command sellItem = new SellItem(peer,user,username);
        Command searchItem = new SearchAndBidItem(peer,user,username);
        Command viewPurchase = new ViewPurchase(peer,user,username);
        Command biddingHistory = new BiddingHistory(peer,user,username);
        Command showMyItem = new ShowMyItem(peer,user,username);
        Command exit = new ExitCommand(peer, gossip);
        Command showObj = new ShowStoredObj(peer,user,username,gossip);


        CommandRegistry registry = new AeshCommandRegistryBuilder()
                .command(numberOfPeers)
                .command(sellItem)
                .command(searchItem)
                .command(viewPurchase)
                .command(biddingHistory)
                .command(showMyItem)
                .command(exit)
                .command(showObj)
                .create();

        AeshConsole aeshConsole = new AeshConsoleBuilder()
                .commandRegistry(registry)
                .settings(settings)
                .prompt(new Prompt(new TerminalString(username + "@p2pbay> ",
                        new TerminalColor(Color.GREEN, Color.DEFAULT, Color.Intensity.BRIGHT))))
                .create();

        aeshConsole.start();

    }

}
