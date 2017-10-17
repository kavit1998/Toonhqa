package me.tfkjake.toonhq.util;

import me.tfkjake.toonhq.ToonHQ;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

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



}
