package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import co.ryred.mcrkit.bungee.UDPPacketSender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RestartProxyCommand extends RTKCommand {

    public RestartProxyCommand( RTKPlugin plugin ) {
        super(plugin, "restartproxy");
    }

    public void execute(CommandSender commandsender, String[] as) {
        if (!(commandsender instanceof ProxiedPlayer)) {
            commandsender.sendMessage(ChatColor.RED + "Only players can do this command!");
        } else {
            ProxiedPlayer player1 = (ProxiedPlayer) commandsender;

            if (as.length != 1) {
                player1.sendMessage(ChatColor.RED + "Usage: /restartproxy <[username:]password>");
            } else {
                if (as[0].contains(":")) {
                    this.plugin.getProxy().getScheduler().runAsync(this.plugin, new UDPPacketSender("restart:" + as[0].trim(), player1, this.plugin, this.port));
                } else {
                    this.plugin.getProxy().getScheduler().runAsync(this.plugin, new UDPPacketSender("restart:" + player1.getName() + ":" + as[0].trim(), player1, this.plugin, this.port));
                }

            }
        }
    }
}
