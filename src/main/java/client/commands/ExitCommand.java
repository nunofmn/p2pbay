package client.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import java.io.IOException;

/**
 * Created by EngSoftware on 26-11-2014.
 */
@CommandDefinition(name="quit", description = "quit the program")
public class ExitCommand implements Command {

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {
        commandInvocation.getShell().out().println("Thank you for choosing p2pBay, see you soon!");
        commandInvocation.stop();
        System.exit(0);
        return CommandResult.SUCCESS;
    }
}
