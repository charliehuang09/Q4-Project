package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Function;

public class PacketManager {

    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;

    // for registering the readPacket() function per packet
    private HashMap<Byte, Function<BufferedReader, ? extends Packet>> deserializers;

    public void registerPacket(Class<? extends Packet> packetClass) {
    }

    public Packet receivePacket() throws IOException {
        byte id = (byte) in.read();
        return deserializers.get(id).apply(in);
    }

    public void connect(String host, int port) throws IOException {
        connection = new Socket(host, port);

        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream(), false);
    }
}