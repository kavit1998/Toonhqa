package me.tfkjake.toonhq.commands.street;

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

public class AddStreet extends AbstractCommand {

    private ToonHQ toonHQ;
    public AddStreet(ToonHQ toonHQ){
        super("addstreet");
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
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: addstreet \"neighbourhood\" \"street\" (E.G. addstreet \"Toontown Central\" \"Silly Street\")").complete());
            return;
        }

        String a = "";
        for(int i = 0; i < args.length; i++){
            a += args[i] + " ";
        }

        String neighbourhood = "";
        String street = "";
        Pattern p = Pattern.compile("\\\"(.*?)\\\"");
        Matcher m = p.matcher(a);
        while(m.find()) {
            if(neighbourhood.isEmpty()) {
                neighbourhood = m.group();
                continue;
            }
            street = m.group();
        }

        if(neighbourhood.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Please surround the neighbourhood name in quotations, e.g. \"Toontown Central\"").complete());
            return;
        }

        if(street.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Please surround the street name in quotations, e.g. \"Silly Street\"").complete());
            return;
        }

        neighbourhood = neighbourhood.replace("\"", "");
        street = street.replace("\"", "");

        List<HashMap<String, Object>> neighbourhoods = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE name = ? AND server_id = ?", neighbourhood, server.getId());
        if(neighbourhoods.size() == 0){
                Util.deleteMessages(10, message.getTextChannel().sendMessage("That neighbourhood doesn't exist!").complete());
                return;
        }

        String nb_id = neighbourhoods.get(0).get("neighbourhood_id").toString();

        List<HashMap<String, Object>> streets = ToonHQ.getMySQL().find("SELECT * FROM streets WHERE name = ? AND server_id = ? AND neighbourhood_id = ?", street, server.getId(), nb_id);
        if(streets.size() > 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("That street already exists in that neighbourhood!").complete());
            return;
        }

        UUID uuid = UUID.randomUUID();

        ToonHQ.getMySQL().add("INSERT INTO streets (server_id, street_id, neighbourhood_id, name) VALUES (?,?,?,?)", server.getId(), uuid.toString(), nb_id, street);

        Util.deleteMessages(10, message.getTextChannel().sendMessage("Street \"" + street + "\" added to \"" + neighbourhood + "\"!").complete());

    }
}
