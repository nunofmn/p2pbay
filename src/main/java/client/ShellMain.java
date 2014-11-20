package client;

import client.commands.*;
import org.jboss.aesh.console.AeshConsole;
import org.jboss.aesh.console.AeshConsoleBuilder;
import org.jboss.aesh.console.Prompt;
import org.jboss.aesh.console.command.registry.AeshCommandRegistryBuilder;
import org.jboss.aesh.console.command.registry.CommandRegistry;
import org.jboss.aesh.console.settings.Settings;
import org.jboss.aesh.console.settings.SettingsBuilder;
import org.jboss.aesh.terminal.Color;
import org.jboss.aesh.terminal.TerminalColor;
import org.jboss.aesh.terminal.TerminalString;

public class ShellMain {

    public static void main(String[] args) {
        SettingsBuilder builder = new SettingsBuilder().logging(true);
        builder.enableMan(false)
                .readInputrc(false);

        Settings settings = builder.create();

        CommandRegistry registry = new AeshCommandRegistryBuilder()
                .command(CreateAccount.class)
                .command(SellItem.class)
                .command(AcceptBid.class)
                .command(SearchItem.class)
                .command(BidItem.class)
                .command(ViewItem.class)
                .command(ViewPurchase.class)
                .command(BiddingHistory.class)
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
