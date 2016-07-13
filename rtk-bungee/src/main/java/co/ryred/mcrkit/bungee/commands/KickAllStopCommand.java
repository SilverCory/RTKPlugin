package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class KickAllStopCommand extends RTKCommand {

    public KickAllStopCommand( RTKPlugin plugin ) {
        super(plugin, "kickallstop");
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            StringBuilder stringbuilder = new StringBuilder("/");

            stringbuilder.append(this.getName());

            for ( String string : args ) {
                stringbuilder.append( " " ).append( string );
            }

            ((ProxiedPlayer) sender).chat(stringbuilder.toString());
        } else {
            String s3 = ((String) this.plugin.getMessageMap().get("toolkit-shutdown-kick-message")).replaceAll(new String(new byte[] { (byte) 92, (byte) 92, (byte) 110}), "\n");

            if (s3 == null) {
                s3 = "Server is shutting down";
            }

            for ( ProxiedPlayer player : this.plugin.getProxy().getPlayers() ) {
                player.disconnect( s3 );
            }

        }
    }
}
