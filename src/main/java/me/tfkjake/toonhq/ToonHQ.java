package me.tfkjake.toonhq;

import me.tfkjake.toonhq.event.UserMessage;
import me.tfkjake.toonhq.util.Properties;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class ToonHQ {

    private JDA jda;
    private static Properties properties;

    public ToonHQ(String token){
        try{
            jda = new JDABuilder(AccountType.BOT).setToken(token).setAutoReconnect(true).buildBlocking();

            // Register Events
            jda.addEventListener(new UserMessage(this));

        }catch(Exception e){
            System.out.println("There was an error starting ToonHQ: " + e.getMessage());
            return;
        }

        properties = new Properties();
    }

    public static void main(String[] args){
        new ToonHQ(properties.getString("token"));
    }

    public JDA getJDA(){
        return jda;
    }

}
