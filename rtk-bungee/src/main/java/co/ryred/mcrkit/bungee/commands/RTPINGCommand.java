package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RTPINGCommand extends RTKCommand {

    public RTPINGCommand(RTKPlugin plugin) {
        super(plugin, "RTPING");
    }

    public void execute(CommandSender commandsender, String[] as) {
        if (!(commandsender instanceof ProxiedPlayer)) {
            this.out.println("RTPONG++");
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
