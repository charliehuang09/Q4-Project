package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import struct.MyHashMap;

public abstract class PacketManager {

  // for registering the readPacket() function per packet
  private MyHashMap<Byte, Class<? extends Packet>> packetMap;

  public PacketManager() {
    this.packetMap = new MyHashMap<Byte, Class<? extends Packet>>();
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
    System.out.println("[network] Receiving packet...");
    byte id = in.readByte();
    int timestamp = in.readInt();
    System.out.println("[network] Received packet with ID: " + id + " and timestamp: " + timestamp);

    try {
      Packet packet = packetMap.get(id).getDeclaredConstructor().newInstance();
      packet.setTimestamp(timestamp);
      packet.read(in);
      return packet;
    } catch (Exception e) {
      throw new RuntimeException("Failed to read packet: " + id);
    }
  }

  public synchronized void sendPacket(Packet packet, DataOutputStream out) throws IOException {
    packet.write(out);
    out.flush();
  }

  public abstract void onReceive(Packet packet);

  public abstract void disconnect();
}