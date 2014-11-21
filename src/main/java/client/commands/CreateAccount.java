package client.commands;

import core.model.UserProfile;
import core.network.PeerConnection;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.io.Resource;
import org.jboss.aesh.terminal.Shell;

import java.io.IOException;
import java.util.List;

public class CreateAccount implements Command {
    @Arguments
    private List<Resource> arguments;

    private Shell shell;

    private PeerConnection peer;

    private UserProfile user;

    private String username;

    public CreateAccount(PeerConnection peer, UserProfile user, String username) {
        super();
        this.peer = peer;
        this.user = user;
        this.username = username;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {


        this.shell = commandInvocation.getShell();

        shell.out().println("Create Account command");

        return null;
    }
}
