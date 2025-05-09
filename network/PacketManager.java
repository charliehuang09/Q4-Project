package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class PacketManager {

    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;

    // for registering the readPacket() function per packet
    private HashMap<Byte, Class<? extends Packet>> packetMap;

    public void registerPacket(Class<? extends Packet> packetClass) {
        try {
            byte id = (byte) packetClass.getDeclaredConstructor().newInstance().getId();
            packetMap.put(id, packetClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register packet: " + packetClass.getName(), e);
        }
    }

    public Packet receivePacket() throws IOException {
        byte id = (byte) in.read();

        try {
            Packet packet = packetMap.get(id).getDeclaredConstructor().newInstance();
            packet.readPacket(in);
            return packet;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read packet: " + id);
        }
    }

    public void sendPacket(Packet packet) {
        char[] buf = new String(packet.asBytes()).toCharArray();
        out.write(buf);
        out.flush();
    }

    public void connect(String host, int port) throws IOException {
        connection = new Socket(host, port);

        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream(), false);
    }
}