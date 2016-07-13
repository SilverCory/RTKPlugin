package co.ryred.mcrkit.bungee;

import co.ryred.mcrkit.bungee.commands.*;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

public class RTKPlugin extends Plugin {

    public String version;
    public static Logger log = null;
    private Properties properties;
    private Map messageMap;
    private Properties config;
    private Properties properties_w;
    private static boolean debuging = false;

    public void onEnable() {

        RTKPlugin.log = this.getLogger();

        try {
            File rtkProperties = new File(getDataFolder(), "rtk.properties");

            if (!rtkProperties.exists()) {
                rtkProperties.createNewFile();


                try ( InputStream is = getResourceAsStream("rtk.properties"); OutputStream os = new FileOutputStream(rtkProperties) ) {
                    ByteStreams.copy( is, os );
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }

            this.config = this.loadProperties(new FileInputStream(rtkProperties));
            RTKPlugin.debuging = Boolean.parseBoolean(this.config.getProperty("debugging"));

            RTKPlugin.log.info( "Debugging is :" + debuging );
        } catch (IOException ioexception) {
            RTKPlugin.log.info("Error loading the plugin config!");
        }

        try {
            this.version = this.getDescription().getVersion();
            this.properties = this.loadProperties(new FileInputStream("toolkit/remote.properties"));
            this.properties_w = this.loadProperties(new FileInputStream("toolkit/wrapper.properties"));

            if ((!this.properties_w.containsKey("stop-command") || !this.properties_w.getProperty("stop-command").equalsIgnoreCase("end")) && this.properties.getProperty("prevent-override", "false").equalsIgnoreCase("false")) {
                this.properties_w.setProperty("stop-command", "end");
                this.properties_w.store(new FileOutputStream("toolkit/wrapper.properties"), "Overwriten by ace!");
            }

            this.messageMap = loadMap(new FileInputStream("toolkit/messages.txt"));
        } catch (IOException ioexception1) {
            RTKPlugin.log.info("Error loading Toolkit plugin properties: " + ioexception1);
            ioexception1.printStackTrace();
        }

        this.registerCommands();
        long l1 = Runtime.getRuntime().totalMemory();
        long l11 = Runtime.getRuntime().maxMemory();

        System.out.println("Memory max: " + l11 + " bytes");
        System.out.println("Memory total: " + l1 + " bytes");
        RTKPlugin.log.info("Remote Toolkit Plugin " + this.version + " enabled!");

    }

    private void registerCommands() {

        PluginManager pm = this.getProxy().getPluginManager();
        pm.registerCommand(this, new HoldProxyCommand(this));
        pm.registerCommand(this, new KickAllCommand(this));
        pm.registerCommand(this, new KickAllHoldCommand(this));
        pm.registerCommand(this, new KickAllStopCommand(this));
        pm.registerCommand(this, new PRescheduleRestartCommand(this));
        pm.registerCommand(this, new RestartProxyCommand(this));
        pm.registerCommand(this, new RTKUnicodeTestCommand(this));
        pm.registerCommand(this, new RTPINGCommand(this));
        pm.registerCommand(this, new SayCommand(this));
        pm.registerCommand(this, new SaveAllCommand());
        pm.registerCommand(this, new PStopWrapperCommand(this));

    }

    public Properties getProperties() {
        return this.properties;
    }

    public Map getMessageMap() {
        return this.messageMap;
    }

    private Properties loadProperties(InputStream inputstream) throws IOException {
        Properties properties1 = new Properties();

        properties1.load(inputstream);
        return properties1;
    }

    public static Map<String, String> loadMap(InputStream inputstream) {
        HashMap<String, String> hashmap = new HashMap<>();
        Scanner scanner = null;

        scanner = new Scanner(inputstream);

        while (scanner.hasNextLine()) {
            String[] as = scanner.nextLine().trim().split(":");
            String s = as[0];
            String s1 = "";

            for (int i = 1; i < as.length; ++i) {
                s1 = s1 + ":" + as[i];
            }

            if (s1.length() > 0) {
                hashmap.put(s, s1.substring(1));
            } else {
                hashmap.put(s, "");
            }
        }

        scanner.close();
        return hashmap;
    }

    public static boolean isDebug() {
        return RTKPlugin.debuging;
    }

    public Properties getConfig() {
        return this.config;
    }
}
