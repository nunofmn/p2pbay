package client.commands;

import org.jboss.aesh.console.*;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import java.io.IOException;

/**
 * Created by EngSoftware on 28-11-2014.
 */
public class PromptForInputHelper {


    public PromptForInputHelper(){

    }


    public String promptForInput(String prompt, Character mask,
                                  CommandInvocation invocation) throws IOException, InterruptedException {

        ConsoleBuffer consoleBuffer = new AeshConsoleBufferBuilder()
                .shell(invocation.getShell())
                .prompt(new Prompt(prompt, mask))
                .create();
        InputProcessor inputProcessor = new AeshInputProcessorBuilder()
                .consoleBuffer(consoleBuffer)
                .create();

        consoleBuffer.displayPrompt();
        String result;
        do {
            result = inputProcessor.parseOperation(invocation.getInput());
        }
        while(result == null );
        return result;
    }
}
