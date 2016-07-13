package co.ryred.mcrkit.bungee;

import net.md_5.bungee.api.plugin.Command;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;

public abstract class RTKCommand extends Command {

    protected RTKPlugin plugin;
    protected PrintStream out;
    protected PrintStream err;
    protected int port;

    public RTKCommand(RTKPlugin plugin, String name) {
        super(name);
        this.plugin = plugin;
        this.out = new PrintStream(new FileOutputStream(FileDescriptor.out));
        this.err = new PrintStream(new FileOutputStream(FileDescriptor.err));

        try {
            this.port = Integer.parseInt(((String) plugin.getProperties().get("remote-control-port")).trim());
        } catch (Exception exception) {
            RTKPlugin.log.log(Level.INFO, "Malformed port: {0}. Using the default.", plugin.getProperties().get("remote-control-port"));
            this.port = 25561;
        }

        plugin.getLogger().info("Registered command: " + this.getName());
    }

    public RTKCommand(RTKPlugin plugin, String name, String permission, String... alias) {
        super(name, permission, alias);
        this.plugin = plugin;
    }
}
