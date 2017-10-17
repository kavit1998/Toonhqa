package me.tfkjake.toonhq.command;

import java.util.ArrayList;
import java.util.List;

public class Command {

    String name;
    AbstractCommand executor;
    List<String> aliases = new ArrayList<>();

    public Command(String name, AbstractCommand executor){
        this.name = name;
        this.executor = executor;
    }

    public String getName(){
        return name;
    }

    public List<String> getAliases(){
        return aliases;
    }

    public void addAlias(String alias){
        if(!aliases.contains(alias))
            aliases.add(alias);
    }

    public void setExecutor(AbstractCommand executor){
        this.executor = executor;
    }

    public AbstractCommand getExecutor(){
        return executor;
    }

}
