package me.tfkjake.toonhq.command;

import me.tfkjake.toonhq.ToonHQ;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;

public class CommandHandler extends ListenerAdapter {

    public void handle(Guild server, Message message, User user, String sMessage){

        String prefix = "hq!"; // You can change this!

        if(!sMessage.startsWith(prefix))
            return;

        if(message.getAuthor().isBot())
            return;

        sMessage = sMessage.substring(prefix.length());

        String[] args = sMessage.split(" ");

        String command = args[0];

        String baseCommand = args[0];

        AbstractCommand cmd = null;

        for(Command c : ToonHQ.getCommandManager().getCommands()){
            if(c.getExecutor().getName().equalsIgnoreCase(command))
                cmd = c.getExecutor();
            if(c.getAliases().contains(command.toLowerCase())){
                baseCommand = c.getName();
                cmd = c.getExecutor();
            }
        }

        Command c = ToonHQ.getCommandManager().getCommand(baseCommand);

        if(cmd == null && c == null)
            return;

        String[] nArgs = new String[args.length - 1];
        for(int i = 1; i < args.length; i++){
            nArgs[i-1] = args[i];
        }

        cmd.execute(server, message, user, nArgs);

    }

}
