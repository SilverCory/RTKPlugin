package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import co.ryred.mcrkit.bungee.UDPPacketSender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HoldProxyCommand extends RTKCommand {

    public HoldProxyCommand( RTKPlugin plugin ) {
        super(plugin, "holdproxy");
    }

    public void execute(CommandSender commandsender, String[] as) {
        if (!(commandsender instanceof ProxiedPlayer)) {
            commandsender.sendMessage(ChatColor.RED + "Only players can do this command!");
        } else {
            ProxiedPlayer player = (ProxiedPlayer) commandsender;

            if (as.length != 1) {
                player.sendMessage(ChatColor.RED + "Usage: /holdproxy <[username:]password>");
            } else {
                if (as[0].contains(":")) {
                    this.plugin.getProxy().getScheduler().runAsync(this.plugin, new UDPPacketSender("hold:" + as[0].trim(), player, this.plugin, this.port));
                } else {
                    this.plugin.getProxy().getScheduler().runAsync(this.plugin, new UDPPacketSender("hold:" + player.getName() + ":" + as[0].trim(), player, this.plugin, this.port));
                }

            }
        }
    }
}
