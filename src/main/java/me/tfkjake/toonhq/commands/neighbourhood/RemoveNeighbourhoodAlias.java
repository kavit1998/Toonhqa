package me.tfkjake.toonhq.commands.neighbourhood;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.AbstractCommand;
import me.tfkjake.toonhq.util.Util;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveNeighbourhoodAlias extends AbstractCommand {

    private ToonHQ toonHQ;
    public RemoveNeighbourhoodAlias(ToonHQ toonHQ){
        super("removeneighbourhoodalias");
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
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: removeneighbourhoodalias \"neighbourhood\" \"alias\" (E.G. removeneighbourhoodalias \"Toontown Central\" \"TTC\")").complete());
            return;
        }

        String a = "";
        for(int i = 0; i < args.length; i++){
            a += args[i] + " ";
        }

        String neighbourhood = "";
        String alias = "";
        Pattern p = Pattern.compile("\\\"(.*?)\\\"");
        Matcher m = p.matcher(a);
        while(m.find()) {
            if(neighbourhood.isEmpty()) {
                neighbourhood = m.group();
                continue;
            }
            alias = m.group();
        }

        if(neighbourhood.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Please surround the neighbourhood name in quotations, e.g. \"Toontown Central\"").complete());
            return;
        }

        if(alias.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: removeneighbourhoodalias \"neighbourhood\" \"alias\" (E.G. removeneighbourhoodalias \"Toontown Central\" \"TTC\")").complete());
            return;
        }

        neighbourhood = neighbourhood.replace("\"", "");
        alias = alias.replace("\"", "");

        List<HashMap<String, Object>> result = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE name = ? AND server_id = ?", neighbourhood, server.getId());

        if(result.size() == 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("That neighbourhood doesn't exist!").complete());
            return;
        }

        List<HashMap<String, Object>> result2 = ToonHQ.getMySQL().find("SELECT * FROM neighbourhood_aliases WHERE server_id = ? AND neighbourhood_id = ?", server.getId(), result.get(0).get("neighbourhood_id").toString());

        if(result2.size() == 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("That alias doesn't exist for that neighbourhood!").complete());
            return;
        }

        ToonHQ.getMySQL().add("DELETE FROM neighbourhood_aliases WHERE server_id = ? AND neighbourhood_id = ? AND alias = ?", server.getId(), result.get(0).get("neighbourhood_id").toString(), alias);

        Util.deleteMessages(10, message.getTextChannel().sendMessage("Removed alias \"" + alias + "\" from \"" + neighbourhood + "\"").complete());

    }
}
