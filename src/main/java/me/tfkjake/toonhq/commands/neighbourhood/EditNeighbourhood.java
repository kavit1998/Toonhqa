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

public class EditNeighbourhood extends AbstractCommand {

    private ToonHQ toonHQ;
    public EditNeighbourhood(ToonHQ toonHQ){
        super("editneighbourhood");
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
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: editneighbourhood \"neighbourhood\" \"new neighbourhood\" (E.G. editneighbourhood \"Toontown Centra\" \"Toontown Central\")").complete());
            return;
        }

        String a = "";
        for(int i = 0; i < args.length; i++){
            a += args[i] + " ";
        }

        String neighbourhood = "";
        String newNeighbourhood = "";
        Pattern p = Pattern.compile("\\\"(.*?)\\\"");
        Matcher m = p.matcher(a);
        while(m.find()) {
            if(neighbourhood.isEmpty())
                neighbourhood = m.group();
            if(!neighbourhood.isEmpty())
                newNeighbourhood = m.group();
        }

        if(neighbourhood.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Please surround the neighbourhood name in quotations, e.g. \"Toontown Central\"").complete());
            return;
        }

        if(newNeighbourhood.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: editneighbourhood \"neighbourhood\" \"new neighbourhood\" (E.G. editneighbourhood \"Toontown Centra\" \"Toontown Central\")").complete());
            return;
        }

        neighbourhood = neighbourhood.replace("\"", "");
        newNeighbourhood = newNeighbourhood.replace("\"", "");

        List<HashMap<String, Object>> result = toonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE name = ? AND server_id = ?", neighbourhood, server.getId());

        if(result.size() == 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("That neighbourhood doesn't exist!").complete());
            return;
        }

        toonHQ.getMySQL().update("UPDATE neighbourhoods SET name = ? WHERE neighbourhood_id = ? AND server_id = ?", newNeighbourhood, result.get(0).get("neighbourhood_id").toString(), server.getId());

        Util.deleteMessages(10, message.getTextChannel().sendMessage("Updated neighbourhood: Set \"" + neighbourhood + "\" to \"" + newNeighbourhood + "\"").complete());

    }
}
