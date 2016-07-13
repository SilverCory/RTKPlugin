package co.ryred.mcrkit.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SaveAllCommand extends Command {

    public SaveAllCommand() {
        super("save-all");
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            System.out.println("Save complete.");
            sender.sendMessage("Save complete.");
        } else {
            StringBuilder sb = new StringBuilder("/");

            sb.append(this.getName());
            int i = args.length;

            for ( String string : args ) {
                sb.append( " " ).append( string );
            }

            ((ProxiedPlayer) sender).chat(sb.toString());
        }
    }
}
