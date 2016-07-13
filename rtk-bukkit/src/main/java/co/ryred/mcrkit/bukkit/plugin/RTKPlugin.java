package co.ryred.mcrkit.bukkit.plugin;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.Terminal;
import org.bukkit.craftbukkit.libs.jline.TerminalSupport;
import org.bukkit.craftbukkit.libs.jline.UnsupportedTerminal;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class RTKPlugin extends JavaPlugin {

    private Properties properties;
    private Map<String, String> messageMap;
    private RTKEventHandler eventHandler;

    public static Map<String, String> loadMap(InputStream inputstream) {
        HashMap<String, String> hashmap = new HashMap<>();

        try (Scanner scanner = new Scanner(inputstream)) {

            while (scanner.hasNextLine()) {
                String[] astring = scanner.nextLine().trim().split(":");
                String s = astring[0];
                String s1 = "";

                for (int i = 1; i < astring.length; ++i) {
                    s1 = s1 + ":" + astring[i];
                }

                if (s1.length() > 0) {
                    hashmap.put(s, s1.substring(1));
                } else {
                    hashmap.put(s, "");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return hashmap;
    }

    public void onEnable() {

        try {
            this.properties = this.loadProperties(new FileInputStream("toolkit/remote.properties"));
            this.messageMap = loadMap(new FileInputStream("toolkit/messages.txt"));
        } catch (IOException ioexception) {
            getLogger().info("Error loading Toolkit plugin properties: " + ioexception);
            ioexception.printStackTrace();
        }

        this.eventHandler = new RTKEventHandler(this, this.properties);
        long i = Runtime.getRuntime().totalMemory();
        long j = Runtime.getRuntime().maxMemory();

        System.out.println("Memory max: " + j + " bytes");
        System.out.println("Memory total: " + i + " bytes");

        try {
            Server server = this.getServer();
            Method method = server.getClass().getMethod("getReader");
            Object object = method.invoke(server);

            if (object instanceof ConsoleReader) {
                Terminal terminal = ((ConsoleReader) object).getTerminal();

                if (terminal instanceof UnsupportedTerminal) {
                    Method method1 = TerminalSupport.class.getDeclaredMethod("setAnsiSupported", Boolean.TYPE);

                    method1.setAccessible(true);
                    method1.invoke(terminal, true);
                }
            }
        } catch (NoClassDefFoundError | Exception noclassdeffounderror) {
            try {
                LegacySupport.initMetrics(this);
            } catch (Throwable throwable1) {
                System.err.println("ERROR LOADING TOOLKIT PLUGIN: " + throwable1);
                throwable1.printStackTrace();
            }
        }

    }

    public void onDisable() {
        getLogger().info("Remote Toolkit Plugin " + getDescription().getVersion() + " disabled!");
    }

    public Properties getProperties() {
        return this.properties;
    }

    public Map<String, String> getMessageMap() {
        return this.messageMap;
    }

    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] astring) {
        return this.eventHandler.onCommand(commandsender, command, s, astring, this);
    }

    private Properties loadProperties(InputStream inputstream) throws IOException {
        Properties properties = new Properties();

        properties.load(inputstream);
        return properties;
    }
}
