package me.tfkjake.toonhq.event;

import me.tfkjake.toonhq.ToonHQ;
import net.dv8tion.jda.core.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;

public class CategoryDelete extends ListenerAdapter {

    private ToonHQ toonHQ;
    public CategoryDelete(ToonHQ toonHQ){
        this.toonHQ = toonHQ;
    }

    @Override
    public void onCategoryDelete(CategoryDeleteEvent e){
        List<HashMap<String, Object>> servers = ToonHQ.getMySQL().find("SELECT * FROM server_config WHERE server_id = ? AND server_key = ?", e.getGuild().getId(), "category_id");
        if(servers.size() == 0)
            return;
        if(e.getGuild().getCategoryById(servers.get(0).get("server_value").toString()) == null)
            ToonHQ.getMySQL().remove("DELETE FROM server_config WHERE server_id = ? AND server_key = ?", e.getGuild().getId(), "category_id");
    }

}
