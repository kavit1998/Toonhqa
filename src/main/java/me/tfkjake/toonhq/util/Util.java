package me.tfkjake.toonhq.util;

import me.tfkjake.toonhq.ToonHQ;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Util {

    public static Color getColorFromHex(String hex){
        int red = Integer.valueOf( hex.substring(0, 2), 16);
        int green = Integer.valueOf( hex.substring(2, 4), 16);
        int blue = Integer.valueOf( hex.substring(4, 6), 16);
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    public static boolean isServerAdmin(String id, String server){
        return ToonHQ.getJDA().getGuildById(server).getMemberById(id).hasPermission(Permission.ADMINISTRATOR)
                || ToonHQ.getJDA().getGuildById(server).getMemberById(id).hasPermission(Permission.MANAGE_SERVER)
                || ToonHQ.getJDA().getGuildById(server).getMemberById(id).hasPermission(Permission.MANAGE_ROLES);
    }

    public static void deleteMessages(int seconds, final Message... messages){
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                for (Message message : messages) {
                    try{
                        message.delete().reason("Auto Delete Message").queue();
                    }catch(Exception e){
                        return; // fail silently
                    }
                }
            }

        }, (seconds*1000));
    }

    public static Optional<TextChannel> getFirstWriteableChannel(Guild server){
        for(TextChannel channel : server.getTextChannels()){
            if(channel.canTalk())
                return Optional.of(channel);
        }
        return Optional.empty();
    }

    public static String getNeighbourhoodFromAlias(String server_id, String alias){
        List<HashMap<String, Object>> aliases = ToonHQ.getMySQL().find("SELECT * FROM neighbourhood_aliases WHERE server_id = ? AND alias = ?", server_id, alias);
        if(aliases.size() == 0)
            return null;

        List<HashMap<String, Object>> neighbourhoods = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE server_id = ? AND neighbourhood_id = ?", server_id, aliases.get(0).get("neighbourhood_id").toString());
        if(neighbourhoods.size() == 0){
            ToonHQ.getMySQL().remove("DELETE FROM neighbourhood_alias WHERE server_id = ? AND neighbourhood_id = ?", server_id, aliases.get(0).get("neighbourhood_id").toString());
            return null;
        }

        return neighbourhoods.get(0).get("name").toString();
    }


}
