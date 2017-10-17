package me.tfkjake.toonhq.event;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.CommandHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UserMessage extends CommandHandler {

    private ToonHQ toonHQ;
    public UserMessage(ToonHQ toonHQ){
        this.toonHQ = toonHQ;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        handle(e.getGuild(), e.getMessage(), e.getAuthor(), e.getMessage().getContent());
    }

}
