package me.tfkjake.toonhq.event;

import me.tfkjake.toonhq.ToonHQ;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildRemoveBot extends ListenerAdapter {

    private ToonHQ toonHQ;
    public GuildRemoveBot(ToonHQ toonHQ){
        this.toonHQ = toonHQ;
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e){
        ToonHQ.getMySQL().remove("DELETE FROM server_config WHERE server_id = ?", e.getGuild().getId());
        ToonHQ.getMySQL().remove("DELETE FROM neighbourhoods WHERE server_id = ?", e.getGuild().getId());
        ToonHQ.getMySQL().remove("DELETE FROM neighbourhood_aliases WHERE server_id = ?", e.getGuild().getId());
        ToonHQ.getMySQL().remove("DELETE FROM streets WHERE server_id = ?", e.getGuild().getId());
    }


}
