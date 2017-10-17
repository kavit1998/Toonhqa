package me.tfkjake.toonhq.commands.neighbourhood;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.AbstractCommand;
import me.tfkjake.toonhq.util.Util;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class Neighbourhoods extends AbstractCommand {

    private ToonHQ toonHQ;
    public Neighbourhoods(ToonHQ toonHQ){
        super("neighbourhoods");
        this.toonHQ = toonHQ;
    }

    @Override
    public void execute(Guild server, Message message, User user, String[] args) {
        Util.deleteMessages(5, message);
        List<HashMap<String, Object>> results = toonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE server_id = ?", server.getId());
        if(results.size() == 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("There are no neighbourhoods!").complete());
            return;
        }

        StringJoiner joiner = new StringJoiner(", ");
        for(HashMap<String, Object> result : results){
            joiner.add(result.get("name").toString());
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Util.getColorFromHex("3F51B5"));
        if(results.size() == 1){
            builder.setTitle("There is " + results.size() + " neighbourhood:");
        }else builder.setTitle("There are " + results.size() + " neighbourhoods:");
        builder.setDescription(joiner.toString());

        Util.deleteMessages(30, message.getTextChannel().sendMessage(builder.build()).complete());

    }
}
