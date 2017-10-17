package me.tfkjake.toonhq.event;

import javafx.scene.control.cell.TextFieldTableCell;
import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.util.Util;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildAddBot extends ListenerAdapter {

    private ToonHQ toonHQ;
    public GuildAddBot(ToonHQ toonHQ){
        this.toonHQ = toonHQ;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        if(Util.getFirstWriteableChannel(e.getGuild()).isPresent()){
            TextChannel channel = Util.getFirstWriteableChannel(e.getGuild()).get();
            channel.sendMessage("Thanks for adding me! To get started, run **hq!setup**.").queue();
        }
    }

}
