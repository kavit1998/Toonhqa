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
        builder.addField("Commands", "help - Display this message." +
                "\nprefix - Change the servers prefix" +
                "\nsetup - Setup the server with the default HQ settings" +
                "\n" +
                "\naddneighbourhood - Add a neighbourhood" +
                "\nremoveneighbourhood - Remove a neighbourhood" +
                "\neditneighbourhood - Edit a neighbourhood" +
                "\nneighbourhoods - List available neighbourhoods" +
                "\naddneighbourhoodalias - Add an alias to a neighbourhood" +
                "\nremoveneighbourhoodalias - Remove an alias from a neighbourhood" +
                "\n" +
                "", true);

        message.getTextChannel().sendMessage(builder.build()).queue();
    }
}
