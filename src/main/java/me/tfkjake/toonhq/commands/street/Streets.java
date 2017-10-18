package me.tfkjake.toonhq.commands.street;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.AbstractCommand;
import me.tfkjake.toonhq.util.Util;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Streets extends AbstractCommand {

    private ToonHQ toonHQ;
    public Streets(ToonHQ toonHQ){
        super("streets");
        this.toonHQ = toonHQ;
    }

    @Override
    public void execute(Guild server, Message message, User user, String[] args) {
        Util.deleteMessages(5, message);
        if(args.length == 0){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Util.getColorFromHex("3F51B5"));
            builder.setDescription("Available Streets:");
            List<HashMap<String, Object>> neighbourhood_results = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE server_id = ?", server.getId());
            for(HashMap<String, Object> result : neighbourhood_results){
                StringJoiner streets = new StringJoiner("\n");
                List<HashMap<String, Object>> streetResult = ToonHQ.getMySQL().find("SELECT * FROM streets WHERE neighbourhood_id = ?", result.get("neighbourhood_id"));
                if(streetResult.size() == 0){
                    streets.add("No Streets Available");
                }else{
                    for(HashMap<String, Object> street : streetResult){
                        streets.add(street.get("name").toString());
                    }
                }
                builder.addField(result.get("name").toString(), streets.toString(), true);
            }
            Util.deleteMessages(30, message.getTextChannel().sendMessage(builder.build()).complete());
        }else{
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

            List<HashMap<String, Object>> neighbourhood_results = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE server_id = ? AND name = ?", server.getId(), neighbourhood);
            if(neighbourhood_results.size() == 0){
                if(Util.getNeighbourhoodFromAlias(server.getId(), neighbourhood) != null){
                    neighbourhood = Util.getNeighbourhoodFromAlias(server.getId(), neighbourhood);
                    neighbourhood_results = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE server_id = ? AND name = ?", server.getId(), neighbourhood);
                }else{
                    Util.deleteMessages(10, message.getTextChannel().sendMessage("That neighbourhood doesn't exist!").complete());
                    return;
                }
            }else{
                neighbourhood = neighbourhood_results.get(0).get("name").toString();
            }
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Util.getColorFromHex("3F51B5"));
            builder.setTitle(neighbourhood);
            StringJoiner streets = new StringJoiner("\n");
            List<HashMap<String, Object>> streetResult = ToonHQ.getMySQL().find("SELECT * FROM streets WHERE neighbourhood_id = ?", neighbourhood_results.get(0).get("neighbourhood_id").toString());
            if(streetResult.size() == 0){
                streets.add("No Streets Available");
            }else{
                for(HashMap<String, Object> street : streetResult){
                    streets.add(street.get("name").toString());
                }
            }
            builder.setDescription(streets.toString());

            Util.deleteMessages(30, message.getTextChannel().sendMessage(builder.build()).complete());
        }

    }
}
