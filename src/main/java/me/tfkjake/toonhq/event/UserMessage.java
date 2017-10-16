package me.tfkjake.toonhq.event;

import me.tfkjake.toonhq.ToonHQ;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserMessage extends ListenerAdapter {

    private ToonHQ toonHQ;
    public UserMessage(ToonHQ toonHQ){
        this.toonHQ = toonHQ;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){

    }

}
