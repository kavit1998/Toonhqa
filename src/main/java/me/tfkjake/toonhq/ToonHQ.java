package me.tfkjake.toonhq;

import me.tfkjake.toonhq.command.CommandManager;
import me.tfkjake.toonhq.commands.Help;
import me.tfkjake.toonhq.commands.Prefix;
import me.tfkjake.toonhq.commands.Setup;
import me.tfkjake.toonhq.commands.neighbourhood.*;
import me.tfkjake.toonhq.commands.street.AddStreet;
import me.tfkjake.toonhq.commands.street.EditStreet;
import me.tfkjake.toonhq.commands.street.RemoveStreet;
import me.tfkjake.toonhq.commands.street.Streets;
import me.tfkjake.toonhq.event.CategoryDelete;
import me.tfkjake.toonhq.event.GuildAddBot;
import me.tfkjake.toonhq.event.GuildRemoveBot;
import me.tfkjake.toonhq.event.UserMessage;
import me.tfkjake.toonhq.util.MySQL;
import me.tfkjake.toonhq.util.Properties;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import java.util.HashMap;
import java.util.List;

public class ToonHQ {

    private static JDA jda;
    private static Properties properties = new Properties();
    private static MySQL mySQL;
    private static CommandManager commandManager;

    public ToonHQ(String token){
        try{
            jda = new JDABuilder(AccountType.BOT).setToken(token).setAutoReconnect(true).buildBlocking();

            // Register Events
            jda.addEventListener(new UserMessage(this));
            jda.addEventListener(new GuildAddBot(this));
            jda.addEventListener(new GuildRemoveBot(this));
            jda.addEventListener(new CategoryDelete(this));

        }catch(Exception e){
            System.out.println("There was an error starting ToonHQ: " + e.getMessage());
            return;
        }

        mySQL = new MySQL(this);
        mySQL.connect();
        commandManager = new CommandManager(this);

        // Create Database Tables
        mySQL.createTable("CREATE TABLE IF NOT EXISTS `commands` (\n\t`id` INT NOT NULL AUTO_INCREMENT,\n\t`command` VARCHAR(50) NOT NULL,\n\tPRIMARY KEY (`id`),\n\tUNIQUE INDEX `command` (`command`)\n)\nCOLLATE='latin1_swedish_ci'\nENGINE=InnoDB\n;");
        mySQL.createTable("CREATE TABLE IF NOT EXISTS `neighbourhoods` (\n\t`id` INT NOT NULL AUTO_INCREMENT,\n\t`server_id` VARCHAR(72) NULL,\n\t`name` VARCHAR(255) NULL,\n\t`neighbourhood_id` VARCHAR(255) NULL,\n\tPRIMARY KEY (`id`),\n\tINDEX `id` (`id`)\n)\nCOLLATE='latin1_swedish_ci'\nENGINE=InnoDB\n;");
        mySQL.createTable("CREATE TABLE IF NOT EXISTS `neighbourhood_aliases` (\n\t`id` INT NOT NULL AUTO_INCREMENT,\n\t`server_id` VARCHAR(72) NULL,\n\t`neighbourhood_id` VARCHAR(72) NULL,\n\t`alias` VARCHAR(72) NULL,\n\tPRIMARY KEY (`id`),\n\tINDEX `id` (`id`)\n)\nCOLLATE='latin1_swedish_ci'\nENGINE=InnoDB\n;");
        mySQL.createTable("CREATE TABLE IF NOT EXISTS `server_config` (\n\t`id` INT NOT NULL AUTO_INCREMENT,\n\t`server_id` VARCHAR(72) NULL,\n\t`server_key` VARCHAR(72) NULL,\n\t`server_value` VARCHAR(72) NULL,\n\tPRIMARY KEY (`id`),\n\tINDEX `id` (`id`)\n)\nCOLLATE='latin1_swedish_ci'\nENGINE=InnoDB\n;");
        mySQL.createTable("CREATE TABLE IF NOT EXISTS `streets` (\n\t`id` INT NOT NULL AUTO_INCREMENT,\n\t`server_id` VARCHAR(72) NULL,\n\t`street_id` VARCHAR(72) NULL,\n\t`neighbourhood_id` VARCHAR(72) NULL,\n\t`name` VARCHAR(72) NULL,\n\tPRIMARY KEY (`id`),\n\tINDEX `id` (`id`)\n)\nCOLLATE='latin1_swedish_ci'\nENGINE=InnoDB\n;");

        // Register commands
        commandManager.registerCommand("help", new Help(this));
        commandManager.registerCommand("prefix", new Prefix(this));
        commandManager.registerCommand("setup", new Setup(this));

        commandManager.registerCommand("addneighbourhood", new AddNeighbourhood(this), "addnbh");
        commandManager.registerCommand("removeneighbourhood", new RemoveNeighbourhood(this), "removenbh");
        commandManager.registerCommand("editneighbourhood", new EditNeighbourhood(this), "editnbh");
        commandManager.registerCommand("neighbourhoods", new Neighbourhoods(this), "hoods", "nbhs");
        commandManager.registerCommand("addneighbourhoodalias", new AddNeighbourhoodAlias(this), "addnbhalias");
        commandManager.registerCommand("removeneighbourhoodalias", new RemoveNeighbourhoodAlias(this), "removenbhalias");

        commandManager.registerCommand("addstreet", new AddStreet(this));
        commandManager.registerCommand("removestreet", new RemoveStreet(this));
        commandManager.registerCommand("editstreet", new EditStreet(this));
        commandManager.registerCommand("streets", new Streets(this));


    }

    public static void main(String[] args){
        new ToonHQ(properties.getString("token"));
    }

    public static Properties getProperties() {
        return properties;
    }

    public static JDA getJDA(){
        return jda;
    }

    public static MySQL getMySQL() {
        return mySQL;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}
