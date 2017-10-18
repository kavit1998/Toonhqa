package me.tfkjake.toonhq.commands.street;

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

public class EditStreet extends AbstractCommand {

    private ToonHQ toonHQ;
    public EditStreet(ToonHQ toonHQ){
        super("editstreet");
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
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: editstreet \"neighbourhood\" \"street\" \"new street\" (E.G. editstreet \"Toontown Central\" \"Silly Stret\" \"Silly Street\")").complete());
            return;
        }

        String a = "";
        for(int i = 0; i < args.length; i++){
            a += args[i] + " ";
        }

        String neighbourhood = "";
        String street = "";
        String newStreet = "";
        Pattern p = Pattern.compile("\\\"(.*?)\\\"");
        Matcher m = p.matcher(a);
        while(m.find()) {
            if(neighbourhood.isEmpty()) {
                neighbourhood = m.group();
                continue;
            }
            if(street.isEmpty()) {
                street = m.group();
                continue;
            }
            if(newStreet.isEmpty()) {
                newStreet = m.group();
                continue;
            }
        }

        if(neighbourhood.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Please surround the neighbourhood name in quotations, e.g. \"Toontown Central\"").complete());
            return;
        }

        if(street.isEmpty() || newStreet.isEmpty()){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("Usage: editstreet \"neighbourhood\" \"street\" \"new street\" (E.G. editstreet \"Toontown Central\" \"Silly Stret\" \"Silly Street\")").complete());
            return;
        }

        neighbourhood = neighbourhood.replace("\"", "");
        street = street.replace("\"", "");
        newStreet = newStreet.replace("\"", "");

        List<HashMap<String, Object>> neighbourhoods = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE name = ? AND server_id = ?", neighbourhood, server.getId());

        if(neighbourhoods.size() == 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("That neighbourhood doesn't exist!").complete());
            return;
        }

        List<HashMap<String, Object>> streets = ToonHQ.getMySQL().find("SELECT * FROM streets WHERE name = ? AND server_id = ? AND neighbourhood_id = ?", street, server.getId(), neighbourhoods.get(0).get("neighbourhood_id").toString());
        System.out.println(street + "," + server.getId() + "," + neighbourhoods.get(0).get("neighbourhood_id").toString());
        if(streets.size() == 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("That street doesn't exist in that neighbourhood!").complete());
            return;
        }

        ToonHQ.getMySQL().update("UPDATE streets SET name = ? WHERE neighbourhood_id = ? AND server_id = ? AND street_id = ?", newStreet, neighbourhoods.get(0).get("neighbourhood_id").toString(), server.getId(), streets.get(0).get("street_id").toString());

        Util.deleteMessages(10, message.getTextChannel().sendMessage("Updated street: Set \"" + street + "\" to \"" + newStreet + "\" in \"" + neighbourhood + "\"").complete());

    }
}
