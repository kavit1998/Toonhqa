package me.tfkjake.toonhq.commands;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.AbstractCommand;
import me.tfkjake.toonhq.util.Util;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.List;

public class Prefix extends AbstractCommand {

    private ToonHQ toonHQ;
    public Prefix(ToonHQ toonHQ){
        super("prefix");
        this.toonHQ = toonHQ;
    }

    @Override
    public void execute(Guild server, Message message, User user, String[] args) {
        Util.deleteMessages(5, message);
        if(!Util.isServerAdmin(user.getId(), server.getId())){
            Util.deleteMessages(5, message.getTextChannel().sendMessage("You don't have permission to use that!").complete());
            return;
        }
        if(args.length == 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("This servers prefix is **" + ToonHQ.getCommandManager().getPrefix(server.getId()) + "**!").complete());
            return;
        }

        String prefix = args[0];

        List<HashMap<String, Object>> servers = ToonHQ.getMySQL().find("SELECT * FROM server_config WHERE server_id = ? AND server_key = ?", server.getId(), "prefix");

        if(servers.size() > 0){
            ToonHQ.getMySQL().update("UPDATE server_config SET server_value = ? WHERE server_id = ? AND server_key = ?", prefix, server.getId(), "prefix");
        }else{
            ToonHQ.getMySQL().add("INSERT INTO server_config (server_id, server_key, server_value) VALUES (?,?,?)", server.getId(), "prefix", prefix);
        }

        Util.deleteMessages(10, message.getTextChannel().sendMessage("Prefix updated!").complete());

    }
}
