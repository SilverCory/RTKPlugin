package co.ryred.mcrkit.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.logging.Level;

public class UDPPacketSender implements Runnable {

    private String s;
    private ProxiedPlayer player;
    private RTKPlugin plugin;
    private int port;
    private final String[] denyStrings = new String[] { "No.", "Make me.", "Nice try!", "It works if you know the password.", "You\'re not being very persuasive.", "You didn\'t say the magic word!", "No! You\'re not my mother.", "Once again, with feeling!", "The password gods have frowned upon you.", "My mother told me not to talk to strangers."};

    public UDPPacketSender(String s, ProxiedPlayer player, RTKPlugin plugin, int port) {
        this.s = s;
        this.player = player;
        this.plugin = plugin;
        this.port = port;
    }

    public void run() {

        try {
            DatagramSocket e = new DatagramSocket();
            DatagramPacket datagrampacket = new DatagramPacket(this.s.getBytes(), this.s.getBytes().length, InetAddress.getByName("localhost"), this.port);

            e.send(datagrampacket);
            byte[] abyte0 = new byte[65536];
            DatagramPacket datagrampacket1 = new DatagramPacket(abyte0, abyte0.length);

            try {
                e.setSoTimeout(700);
                e.receive(datagrampacket1);
                String exception1 = new String(datagrampacket1.getData());

                if (exception1.trim().equalsIgnoreCase("response:success")) {
                    this.player.sendMessage(ChatColor.DARK_BLUE + "[PROXY] " + ChatColor.RED + "Done!");
                } else if (exception1.trim().equalsIgnoreCase("response:command_error")) {
                    this.player.sendMessage(ChatColor.DARK_BLUE + "[PROXY] " + ChatColor.RED + "Invalid restart time!");
                } else {
                    this.player.sendMessage(ChatColor.DARK_BLUE + "[PROXY] " + ChatColor.RED + this.denyStrings[(int) (Math.random() * (double) this.denyStrings.length)]);
                }
            } catch (SocketTimeoutException sockettimeoutexception) {
                e.close();
                this.player.sendMessage(ChatColor.DARK_BLUE + "[PROXY] " + ChatColor.RED + this.denyStrings[(int) (Math.random() * (double) this.denyStrings.length)]);
            } catch (Exception exception) {
                e.close();
                RTKPlugin.log.log(Level.INFO, "Unexpected Socket error: {0}", exception);
                exception.printStackTrace();
            }

            e.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            RTKPlugin.log.log(Level.INFO, "Error in Remote Toolkit plugin: {0}", ioexception);
        }

    }
}
