package me.tfkjake.toonhq.commands;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.AbstractCommand;
import me.tfkjake.toonhq.util.Util;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.StringJoiner;

public class Help extends AbstractCommand {

    private ToonHQ toonHQ;
    public Help(ToonHQ toonHQ){
        super("help");
    }

    @Override
    public void execute(Guild server, Message message, User user, String[] args) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Util.getColorFromHex("3F51B5"));
        builder.setAuthor("ToonHQ", null, "https://i.imgur.com/ZyA9ZTm.png");
        builder.setDescription("A JDA Bot written by Jake S. The source can be found at https://goo.gl/Cbniku");
        builder.addField("General Commands", "help - Display this message." +
                "\nprefix - Change the servers prefix" +
                "\nsetup - Setup the server with the default HQ settings",false);
        builder.addField("Neighbourhoods", "addneighbourhood - Add a neighbourhood" +
                "\nremoveneighbourhood - Remove a neighbourhood" +
                "\neditneighbourhood - Edit a neighbourhood" +
                "\naddneighbourhoodalias - Add an alias to a neighbourhood" +
                "\nremoveneighbourhoodalias - Remove an alias from a neighbourhood" +
                "\nneighbourhoods - List available neighbourhoods", false);
        builder.addField("Streets", "addstreet - Add a street" +
                "\nremovestreet - Remove a street" +
                "\neditstreet - Edit a street" +
                "\nstreets - List available streets (accepts \"neighbourhood\"", false);

        message.getTextChannel().sendMessage(builder.build()).queue();
    }
}
