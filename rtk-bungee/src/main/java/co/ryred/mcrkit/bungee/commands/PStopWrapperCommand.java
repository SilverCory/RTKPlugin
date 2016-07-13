package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import co.ryred.mcrkit.bungee.UDPPacketSender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PStopWrapperCommand extends RTKCommand {

    public PStopWrapperCommand( RTKPlugin plugin ) {
        super(plugin, "pstopwrapper");
    }

    public void execute(CommandSender commandsender, String[] as) {
        if (!(commandsender instanceof ProxiedPlayer)) {
            commandsender.sendMessage(ChatColor.RED + "Only players can do this command!");
        } else {
            ProxiedPlayer player3 = (ProxiedPlayer) commandsender;

            if (as.length != 1) {
                player3.sendMessage(ChatColor.RED + "Usage: /pstopwrapper <[username:]password>");
            } else {
                if (as[0].contains(":")) {
                    this.plugin.getProxy().getScheduler().runAsync(this.plugin, new UDPPacketSender("stopwrapper:" + as[0].trim(), player3, this.plugin, this.port));
                } else {
                    this.plugin.getProxy().getScheduler().runAsync(this.plugin, new UDPPacketSender("stopwrapper:" + player3.getName() + ":" + as[0].trim(), player3, this.plugin, this.port));
                }

            }
        }
    }
}
