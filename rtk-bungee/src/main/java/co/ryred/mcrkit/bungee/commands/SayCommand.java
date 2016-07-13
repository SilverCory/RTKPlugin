package co.ryred.mcrkit.bungee.commands;

import co.ryred.mcrkit.bungee.RTKCommand;
import co.ryred.mcrkit.bungee.RTKPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SayCommand extends RTKCommand {

    public SayCommand(RTKPlugin plugin) {
        super(plugin, "say");
    }

    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {
            StringBuilder builder = new StringBuilder("/");
            builder.append(this.getName());

            for ( String arg : args) {
                builder.append(" ").append(arg);
            }

            ((ProxiedPlayer) sender).chat(builder.toString());
        } else {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "You must supply a message.");
            } else {
                StringBuilder builder = new StringBuilder();
                if (args[0].startsWith("&h")) {
                    args[0] = args[0].substring(2, args[0].length());
                } else {
                    builder.append(ProxyServer.getInstance().getTranslation("alert", new Object[0]));
                }

                for (String arg : args) {
                    builder.append(ChatColor.translateAlternateColorCodes('&', arg));
                    builder.append(" ");
                }

                String s = builder.substring(0, builder.length() - 1);

                ProxyServer.getInstance().broadcast(s);
            }

        }
    }
}
