package me.tfkjake.toonhq;

import me.tfkjake.toonhq.command.CommandManager;
import me.tfkjake.toonhq.commands.Help;
import me.tfkjake.toonhq.event.UserMessage;
import me.tfkjake.toonhq.util.MySQL;
import me.tfkjake.toonhq.util.Properties;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class ToonHQ {

    private JDA jda;
    private static Properties properties;
    private MySQL mySQL;
    private static CommandManager commandManager;

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
        mySQL = new MySQL(this);
        mySQL.connect();
        commandManager = new CommandManager(this);

        // Register commands
        commandManager.registerCommand("help", new Help(this));

    }

    public static void main(String[] args){
        new ToonHQ(properties.getString("token"));
    }

    public static Properties getProperties() {
        return properties;
    }

    public JDA getJDA(){
        return jda;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}
