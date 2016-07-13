package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import co.ryred.mcrkit.bungee.UDPPacketSender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PRescheduleRestartCommand extends RTKCommand {

    public PRescheduleRestartCommand( RTKPlugin plugin ) {
        super(plugin, "preschedulerestart");
    }

    public void execute(CommandSender commandsender, String[] as) {
        if (!(commandsender instanceof ProxiedPlayer)) {
            commandsender.sendMessage(ChatColor.RED + "Only players can do this command!");
        } else {
            ProxiedPlayer player2 = (ProxiedPlayer) commandsender;

            if (as.length < 2) {
                player2.sendMessage(ChatColor.RED + "Usage: /preschedulerestart <[username:]password> <time>");
            } else {
                String s4 = "";

                for (int l = 1; l < as.length; ++l) {
                    s4 = s4 + as[l].trim() + " ";
                }

                s4 = s4.replaceAll(":", "-");
                if (as[0].contains(":")) {
                    this.plugin.getProxy().getScheduler().runAsync(this.plugin, new UDPPacketSender("reschedule:" + s4.trim() + ":" + as[0].trim(), player2, this.plugin, this.port));
                } else {
                    this.plugin.getProxy().getScheduler().runAsync(this.plugin, new UDPPacketSender("reschedule:" + s4.trim() + ":" + player2.getName() + ":" + as[0].trim(), player2, this.plugin, this.port));
                }

            }
        }
    }
}
