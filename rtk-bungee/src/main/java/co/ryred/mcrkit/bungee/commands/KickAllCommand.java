package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class KickAllCommand extends RTKCommand {

    public KickAllCommand( RTKPlugin plugin ) {
        super(plugin, "kickall");
    }

    public void execute(CommandSender commandsender, String[] as) {
        if (commandsender instanceof ProxiedPlayer) {
            StringBuilder stringbuilder = new StringBuilder("/");

            stringbuilder.append(this.getName());

            for ( String string : as ) {
                stringbuilder.append( " " ).append( string );
            }

            ((ProxiedPlayer) commandsender).chat(stringbuilder.toString());
        } else {
            String s1 = ((String) this.plugin.getMessageMap().get("restart-kick-message")).replaceAll(new String(new byte[] { (byte) 92, (byte) 92, (byte) 110}), "\n");

            if (s1 == null) {
                s1 = "Everyone is being kicked ;)";
            }

            for ( ProxiedPlayer player : this.plugin.getProxy().getPlayers() ) {
                player.disconnect( s1 );
            }

        }
    }
}
