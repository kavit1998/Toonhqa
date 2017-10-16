package me.tfkjake.toonhq.command;

import me.tfkjake.toonhq.ToonHQ;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;

public class CommandHandler extends ListenerAdapter{

    public boolean handle(Guild server, Message message, User user, String sMessage){

        String prefix = "~"; // You can change this!

        if(!sMessage.startsWith(prefix))
            return false;

        if(message.getAuthor().isBot())
            return false;

        sMessage = sMessage.substring(prefix.length());

        String[] args = sMessage.split(" ");

        String command = args[0];

        String baseCommand = args[0];

        System.out.println(command);

        AbstractCommand cmd = null;

        for(Command c : ToonHQ.getCommandManager().getCommands()){
            if(c.getExecutor().getName().equalsIgnoreCase(command))
                cmd = c.getExecutor();
        }

        System.out.println(command);

        Command c = ToonHQ.getCommandManager().getCommand(baseCommand);

        if(cmd == null && c == null)
            return false;

        System.out.println(command);

        System.out.println(command);

        String[] nArgs = new String[args.length - 1];
        for(int i = 1; i < args.length; i++){
            nArgs[i-1] = args[i];
        }
        return true;
    }

}
