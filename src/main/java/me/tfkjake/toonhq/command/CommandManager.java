package me.tfkjake.toonhq.command;

import me.tfkjake.toonhq.ToonHQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager {

    List<Command> commands = new ArrayList<>();

    private ToonHQ toonHQ;
    public CommandManager(ToonHQ toonHQ){
        this.toonHQ = toonHQ;
    }

    public Command getCommand(String name){
        for(Command c : commands){
            if(c.getName().equalsIgnoreCase(name))
                return c;
        }
        return null;
    }

    public List<Command> getCommands(){
        return commands;
    }

    public void registerCommand(String name, AbstractCommand executor){
        if(getCommand(name) != null) return;
        List<HashMap<String, Object>> commands = toonHQ.getMySQL().find("SELECT * FROM commands WHERE command = ?", name);
        if(commands.isEmpty()){
            toonHQ.getMySQL().add("INSERT INTO commands (command, help) VALUES (?, ?)", name, "Some help here");
            Command command = new Command(name, executor);
            this.commands.add(command);
            System.out.println("Command \"" + name + "\" added to the database!");
        }else{
            Command command = new Command(name, executor);
            this.commands.add(command);
            System.out.println("Command \"" + name + "\" registered!");
        }
    }

}
