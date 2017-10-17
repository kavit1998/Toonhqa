package me.tfkjake.toonhq.commands.neighbourhood;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.AbstractCommand;
import me.tfkjake.toonhq.util.Util;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveNeighbourhood extends AbstractCommand {

    private ToonHQ toonHQ;
    public RemoveNeighbourhood(ToonHQ toonHQ){
        super("removeneighbourhood");
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
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: removeneighbourhood \"neighbourhood\" (E.G. removeneighbourhood \"Toontown Central\") [This will delete all streets associated with it!]").complete());
            return;
        }

        String a = "";
        for(int i = 0; i < args.length; i++){
            a += args[i] + " ";
        }

        String neighbourhood = "";
        Pattern p = Pattern.compile("\\\"(.*?)\\\"");
        Matcher m = p.matcher(a);
        while(m.find()) {
            neighbourhood = m.group(0);
        }

        if(neighbourhood.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Please surround the neighbourhood name in quotations, e.g. \"Toontown Central\"").complete());
            return;
        }

        neighbourhood = neighbourhood.replace("\"", "");

        List<HashMap<String, Object>> result = toonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE name = ? AND server_id = ?", neighbourhood, server.getId());
        if(result.size() ==  0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("That neighbourhood doesn't exist!").complete());
            return;
        }

        toonHQ.getMySQL().add("DELETE FROM neighbourhoods WHERE name = ? AND server_id = ?", neighbourhood, server.getId());

        // TODO: Remove all streets with that neighbourhood

        Util.deleteMessages(10, message.getTextChannel().sendMessage("Neighbourhood \"" + neighbourhood + "\" removed!").complete());

    }
}
