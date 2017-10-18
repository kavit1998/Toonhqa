package me.tfkjake.toonhq.commands;

import me.tfkjake.toonhq.ToonHQ;
import me.tfkjake.toonhq.command.AbstractCommand;
import me.tfkjake.toonhq.util.Util;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class Setup extends AbstractCommand {

    private ToonHQ toonHQ;
    public Setup(ToonHQ toonHQ){
        super("setup");
        this.toonHQ = toonHQ;
    }

    @Override
    public void execute(Guild server, Message message, User user, String[] args) {
        Util.deleteMessages(5, message);
        if(!Util.isServerAdmin(user.getId(), server.getId())){
            Util.deleteMessages(5, message.getTextChannel().sendMessage("You don't have permission to use that!").complete());
            return;
        }
        List<HashMap<String, Object>> neighbourhoods = ToonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE server_id = ?", server.getId());
        List<HashMap<String, Object>> servers = ToonHQ.getMySQL().find("SELECT * FROM server_config WHERE server_id = ? AND server_key = ?", server.getId(), "category_id");

        //List<HashMap<String, Object>> streets = toonHQ.getMySQL().find("SELECT * FROM neighbourhoods WHERE server_id = ?", server.getId());
        if(neighbourhoods.size() > 0 || servers.size() > 0){
            Util.deleteMessages(10, message.getTextChannel().sendMessage("It seems this server is already setup!").complete());
            return;
        }

        try {
            Channel category = server.getController().createCategory("Group Chats").complete();
            ToonHQ.getMySQL().add("INSERT INTO server_config (server_id, server_key, server_value) VALUES (?,?,?)", server.getId(), "category_id", category.getId());
        }catch(PermissionException e){
            message.getTextChannel().sendMessage("I don't have permission to create categories! For best results, give me ADMINISTRATOR.").queue();
            return;
        }

        StringJoiner neighbourhood = new StringJoiner(",");
        String TTC = UUID.randomUUID().toString();
        String DD = UUID.randomUUID().toString();
        String DG = UUID.randomUUID().toString();
        String MML = UUID.randomUUID().toString();
        String TB = UUID.randomUUID().toString();
        String DDL = UUID.randomUUID().toString();

        neighbourhood.add("(\"" + server.getId() + "\", \"Toontown Central\", \"" + TTC + "\")");
        neighbourhood.add("(\"" + server.getId() + "\", \"Donald\'s Dock\", \"" + DD + "\")");
        neighbourhood.add("(\"" + server.getId() + "\", \"Daisy Gardens\", \"" + DG + "\")");
        neighbourhood.add("(\"" + server.getId() + "\", \"Minnie\'s Melodyland\", \"" + MML + "\")");
        neighbourhood.add("(\"" + server.getId() + "\", \"The Brrrgh\", \"" + TB + "\")");
        neighbourhood.add("(\"" + server.getId() + "\", \"Donald\'s Dreamland\", \"" + DDL + "\")");

        StringJoiner neighbourhood_alias = new StringJoiner(",");
        neighbourhood_alias.add("('" + server.getId() + "', 'TTC', '" + TTC + "')");
        neighbourhood_alias.add("('" + server.getId() + "', 'DD', '" + DD + "')");
        neighbourhood_alias.add("('" + server.getId() + "', 'DG', '" + DG + "')");
        neighbourhood_alias.add("('" + server.getId() + "', 'MML', '" + MML + "')");
        neighbourhood_alias.add("('" + server.getId() + "', 'TB', '" + TB + "')");
        neighbourhood_alias.add("('" + server.getId() + "', 'DDL', '" + DDL + "')");

        StringJoiner TTCStreets = new StringJoiner(",");
        StringJoiner DDStreets = new StringJoiner(",");
        StringJoiner DGStreets = new StringJoiner(",");
        StringJoiner MMLStreets = new StringJoiner(",");
        StringJoiner TBStreets = new StringJoiner(",");
        StringJoiner DDLStreets = new StringJoiner(",");

        TTCStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + TTC + "\", \"Silly Street\")");
        TTCStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + TTC + "\", \"Loopy Lane\")");
        TTCStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + TTC + "\", \"Punchline Place\")");

        DDStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + DD + "\", \"Barnacle Boulevard\")");
        DDStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + DD + "\", \"Seaweed Street\")");
        DDStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + DD + "\", \"Lighthouse Lane\")");

        DGStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + DG + "\", \"Elm Street\")");
        DGStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + DG + "\", \"Maple Street\")");
        DGStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + DG + "\", \"Oak Street\")");

        MMLStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + MML + "\", \"Alto Avenue\")");
        MMLStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + MML + "\", \"Baritone Boulevard\")");
        MMLStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + MML + "\", \"Tenor Terrace\")");

        TBStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + TB + "\", \"Sleet Street\")");
        TBStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + TB + "\", \"Walrus Way\")");
        TBStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + TB + "\", \"Polar Place\")");

        DDLStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + DDL + "\", \"Lullaby Lane\")");
        DDLStreets.add("(\"" + server.getId() + "\", \"" + UUID.randomUUID().toString() + "\", \"" + DDL + "\", \"Pajama Place\")");

        ToonHQ.getMySQL().add("INSERT INTO neighbourhoods (server_id, name, neighbourhood_id) VALUES " + neighbourhood.toString());
        ToonHQ.getMySQL().add("INSERT INTO neighbourhood_aliases (server_id, alias, neighbourhood_id) VALUES " + neighbourhood_alias.toString());
        ToonHQ.getMySQL().add("INSERT INTO streets (server_id, street_id, neighbourhood_id, name) VALUES " + TTCStreets + "," + DDStreets + "," + DGStreets + "," + MMLStreets + "," + TBStreets + "," + DDLStreets);


        Util.deleteMessages(10, message.getTextChannel().sendMessage("Successfully completed default setup!").complete());
    }
}
