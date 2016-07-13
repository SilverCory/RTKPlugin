package co.ryred.mcrkit.bukkit.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;

public class RTKEventHandler {

    private final String[] denyStrings = new String[]{"No.", "Make me.", "Nice try!", "It works if you know the password.", "You\'re not being very persuasive.", "You didn\'t say the magic word!", "No! You\'re not my mother.", "Once again, with feeling!", "The password gods have frowned upon you.", "My mother told me not to talk to strangers."};
    private RTKPlugin plugin;
    private int port = 25561;
    private PrintStream out;
    private PrintStream err;

    public RTKEventHandler(RTKPlugin plugin, Properties properties) {
        this.plugin = plugin;
        this.out = new PrintStream(new FileOutputStream(FileDescriptor.out));
        this.err = new PrintStream(new FileOutputStream(FileDescriptor.err));

        try {
            this.port = Integer.parseInt(((String) properties.get("remote-control-port")).trim());
        } catch (Exception exception) {
            plugin.getLogger().log(Level.INFO, "Malformed port: {0}. Using the default.", properties.get("remote-control-port"));
            this.port = 25561;
        }

    }

    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] astring, RTKPlugin rtkplugin) {
        if (command.getName().equals("rtkunicodetest")) {
            this.err.println("AêñüC");
            commandsender.sendMessage(ChatColor.YELLOW + "A" + "ê" + "ñ" + "ü" + "C");
        } else {
            if (commandsender instanceof ConsoleCommandSender) {
                String s1;
                Collection<? extends Player> players = rtkplugin.getServer().getOnlinePlayers();

                if (command.getName().equals("kickall")) {
                    s1 = rtkplugin.getMessageMap().get("restart-kick-message").replace("\\n", "\n");
                    if (s1 == null) {
                        s1 = "Everyone is being kicked ;)";
                    }

                    for (Player player : players) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', s1));
                    }

                    return true;
                }

                if (command.getName().equals("kickallhold")) {
                    s1 = rtkplugin.getMessageMap().get("hold-kick-message").replace("\\n", "\n");
                    if (s1 == null) {
                        s1 = "Server is shutting down";
                    }

                    for (Player player : players) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', s1));
                    }

                    return true;
                }

                if (command.getName().equals("kickallstop")) {
                    s1 = rtkplugin.getMessageMap().get("toolkit-shutdown-kick-message").replace("\\n", "\n");
                    if (s1 == null) {
                        s1 = "Server is shutting down";
                    }

                    for (Player player : players) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', s1));
                    }

                    return true;
                }

                if (command.getName().equals("RTPING")) {
                    this.out.println("RTPONG++");
                    return true;
                }
            } else if (commandsender instanceof Player) {
                Player player1 = (Player) commandsender;

                if (s.equalsIgnoreCase("holdsrv")) {
                    if (astring.length != 1) {
                        player1.sendMessage(ChatColor.RED + "Usage: /holdsrv <[username:]password>");
                        return true;
                    }

                    if (astring[0].contains(":")) {
                        this.dispatchLocalUDPPacket("hold:" + astring[0].trim(), player1);
                    } else {
                        this.dispatchLocalUDPPacket("hold:" + player1.getName() + ":" + astring[0].trim(), player1);
                    }

                    return true;
                }

                if (s.equalsIgnoreCase("restartsrv")) {
                    player1 = (Player) commandsender;
                    if (astring.length != 1) {
                        player1.sendMessage(ChatColor.RED + "Usage: /restartsrv <[username:]password>");
                        return true;
                    }

                    if (astring[0].contains(":")) {
                        this.dispatchLocalUDPPacket("restart:" + astring[0].trim(), player1);
                    } else {
                        this.dispatchLocalUDPPacket("restart:" + player1.getName() + ":" + astring[0].trim(), player1);
                    }

                    return true;
                }

                if (s.equalsIgnoreCase("reschedulerestart")) {
                    player1 = (Player) commandsender;
                    if (astring.length < 2) {
                        player1.sendMessage(ChatColor.RED + "Usage: /reschedulerestart <[username:]password> <time>");
                        return true;
                    }

                    String s2 = "";

                    for (int i = 1; i < astring.length; ++i) {
                        s2 = s2 + astring[i].trim() + " ";
                    }

                    s2 = s2.replaceAll(":", "-");
                    if (astring[0].contains(":")) {
                        this.dispatchLocalUDPPacket("reschedule:" + s2.trim() + ":" + astring[0].trim(), player1);
                    } else {
                        this.dispatchLocalUDPPacket("reschedule:" + s2.trim() + ":" + player1.getName() + ":" + astring[0].trim(), player1);
                    }

                    return true;
                }

                if (s.equalsIgnoreCase("stopwrapper")) {
                    player1 = (Player) commandsender;
                    if (astring.length != 1) {
                        player1.sendMessage(ChatColor.RED + "Usage: /stopwrapper <[username:]password>");
                        return true;
                    }

                    if (astring[0].contains(":")) {
                        this.dispatchLocalUDPPacket("stopwrapper:" + astring[0].trim(), player1);
                    } else {
                        this.dispatchLocalUDPPacket("stopwrapper:" + player1.getName() + ":" + astring[0].trim(), player1);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private void dispatchLocalUDPPacket(String s, final Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                final DatagramSocket datagramsocket = new DatagramSocket();
                DatagramPacket datagrampacket = new DatagramPacket(s.getBytes(), s.getBytes().length, InetAddress.getByName("localhost"), RTKEventHandler.this.port);

                datagramsocket.send(datagrampacket);
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {

                    byte[] abyte = new byte[65536];
                    DatagramPacket datagrampacket1 = new DatagramPacket(abyte, abyte.length);

                    try {
                        datagramsocket.setSoTimeout(700);
                        datagramsocket.receive(datagrampacket1);
                        String s1 = new String(datagrampacket1.getData());

                        if (s1.trim().equalsIgnoreCase("response:success")) {
                            player.sendMessage(ChatColor.RED + "Done!");
                        } else if (s1.trim().equalsIgnoreCase("response:command_error")) {
                            player.sendMessage(ChatColor.RED + "Invalid args!");
                        } else {
                            player.sendMessage(ChatColor.RED + RTKEventHandler.this.denyStrings[(int) (Math.random() * (double) RTKEventHandler.this.denyStrings.length)]);
                        }
                    } catch (SocketTimeoutException sockettimeoutexception) {
                        player.sendMessage(ChatColor.RED + RTKEventHandler.this.denyStrings[(int) (Math.random() * (double) RTKEventHandler.this.denyStrings.length)]);
                    } catch (Exception exception) {
                        plugin.getLogger().log(Level.INFO, "Unexpected Socket error: {0}", exception);
                        exception.printStackTrace();
                    }

                });
            } catch (Exception exception) {
                plugin.getLogger().log(Level.INFO, "Error in Remote Toolkit plugin: {0}", exception);
                exception.printStackTrace();
            }
        });
    }
}
