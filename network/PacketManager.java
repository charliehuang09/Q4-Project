package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public abstract class PacketManager implements Runnable {

    private Socket connection;
    private DataInputStream in;
    private DataOutputStream out;

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
        byte id = in.readByte();
        int timestamp = in.readInt();

        try {
            Packet packet = packetMap.get(id).getDeclaredConstructor().newInstance();
            packet.setTimestamp(timestamp);
            packet.read(in);
            return packet;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read packet: " + id);
        }
    }
    
    public void sendPacket(Packet packet) {
        try {
            packet.write(out);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending packet: " + packet.getClass().getName());
        }
    }
    
    public abstract void onReceive(Packet packet);
    
    public void connect(String host, int port) throws IOException {
        connection = new Socket(host, port);
        
        in = new DataInputStream(connection.getInputStream());
        out = new DataOutputStream(connection.getOutputStream());
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            while (true) {
                Packet packet = receivePacket();
                onReceive(packet);
            }
        } catch (IOException e) {
            e.printStackTrace(); // for temporary debug
            disconnect();
        }
    }
}