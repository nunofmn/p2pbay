package client.commands;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.io.Resource;
import org.jboss.aesh.terminal.Shell;

import java.io.IOException;
import java.util.List;

@CommandDefinition(name="item", description ="see an item info")
public class ViewItem implements Command {

    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {


        this.shell = commandInvocation.getShell();

        shell.out().println("View item command");

        return null;
    }
}