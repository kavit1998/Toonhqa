package me.tfkjake.toonhq.commands;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.AbstractCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public class Help extends AbstractCommand {

    private ToonHQ toonHQ;
    public Help(ToonHQ toonHQ){
        super("help");
    }

    @Override
    public void execute(Guild server, Message message, User user, String[] args) {
        message.getTextChannel().sendMessage("rawr xd").queue();
    }
}
