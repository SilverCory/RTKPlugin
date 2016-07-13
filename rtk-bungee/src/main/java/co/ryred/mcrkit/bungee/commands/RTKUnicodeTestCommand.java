package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RTKUnicodeTestCommand extends RTKCommand {

    public RTKUnicodeTestCommand( RTKPlugin plugin ) {
        super(plugin, "rtkunicodetest");
    }

    public void execute(CommandSender commandsender, String[] as) {
        if (!(commandsender instanceof ProxiedPlayer)) {
            this.err.println( "AêñüC" );
            commandsender.sendMessage( ChatColor.YELLOW + "A" + "ê" + "ñ" + "ü" + "C" );
        } else {
            StringBuilder sb = new StringBuilder("/");

            sb.append(this.getName());

            for ( String string : as ) {
                sb.append( " " ).append( string );
            }

            ((ProxiedPlayer) commandsender).chat(sb.toString());
        }
    }
}
