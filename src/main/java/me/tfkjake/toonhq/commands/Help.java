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
        builder.setColor(Util.getColorFromHex("9C27B0"));
        builder.setAuthor("ToonHQ", null, "https://i.imgur.com/ZyA9ZTm.png");
        builder.setDescription("A JDA Bot written by Jake S. The source can be found at https://goo.gl/Cbniku");
        builder.addField("Commands", "help - Display this message." +
                "\n\naddneighbourhood - Add a neighbourhood - addneighbourhood \"neighbourhood\"" +
                "\n\nremoveneighbourhood - Remove a neighbourhood - removeneighbourhood \"neighbourhood\" (This will delete all streets in this neighbourhood)" +
                "\n\neditneighbourhood - Edit a neighbourhood - editneighbourhood \"neighbourhood\" \"new neighbourhood\"" +
                "\n\nneighbourhoods - List available neighbourhoods", true);

        message.getTextChannel().sendMessage(builder.build()).queue();
    }
}
