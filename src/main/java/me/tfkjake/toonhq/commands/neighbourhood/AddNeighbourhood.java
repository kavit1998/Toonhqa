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

public class AddNeighbourhood extends AbstractCommand {

    private ToonHQ toonHQ;
    public AddNeighbourhood(ToonHQ toonHQ){
        super("addneighbourhood");
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
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: addneighbourhood \"neighbourhood\" (E.G. addneighbourhood \"Toontown Central\")").complete());
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
            neighbourhood = m.group();
        }

        if(neighbourhood.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Please surround the neighbourhood name in quotations, e.g. \"Toontown Central\"").complete());
            return;
        }

        neighbourhood = neighbourhood.replace("\"", "");

        List<HashMap<String, Object>> result = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE name = ? AND server_id = ?", neighbourhood, server.getId());
        if(result.size() > 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("That neighbourhood already exists in the database!").complete());
            return;
        }

        UUID uuid = UUID.randomUUID();

        ToonHQ.getMySQL().add("INSERT INTO neighbourhoods (server_id, name, neighbourhood_id) VALUES (?,?,?)", server.getId(), neighbourhood, uuid.toString());

        Util.deleteMessages(10, message.getTextChannel().sendMessage("Neighbourhood \"" + neighbourhood + "\" added!").complete());

    }
}
