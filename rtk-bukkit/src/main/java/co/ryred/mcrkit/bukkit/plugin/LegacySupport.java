package co.ryred.mcrkit.bukkit.plugin;

import jline.Terminal;
import jline.TerminalSupport;
import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public class LegacySupport {

    public static void initMetrics(JavaPlugin javaplugin) throws Throwable {
        Server server = javaplugin.getServer();
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

    }
}
