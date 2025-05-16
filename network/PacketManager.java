package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public abstract class PacketManager {

  // for registering the readPacket() function per packet
  private HashMap<Byte, Class<? extends Packet>> packetMap;

  public PacketManager() {
    this.packetMap = new HashMap<Byte, Class<? extends Packet>>();
  }

  public void registerPacket(Class<? extends Packet> packetClass) {
    try {
      byte id = (byte) packetClass.getDeclaredConstructor().newInstance().getId();
      packetMap.put(id, packetClass);
    } catch (Exception e) {
      throw new RuntimeException("Failed to register packet: " + packetClass.getName(), e);
    }
  }

  public Packet receivePacket(DataInputStream in) throws IOException {
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

  public void sendPacket(Packet packet, DataOutputStream out) throws IOException {
    packet.write(out);
    out.flush();
  }

  public abstract void onReceive(Packet packet);

  public abstract void disconnect();
}