package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Packet;
import network.PacketManager;
import network.packets.JoinRequestPacket;
import network.packets.ReadyUpPacket;
import network.packets.SetPlayerIdPacket;
import network.packets.TeamSelectionPacket;
import network.packets.UpdatePosPacket;

public class IndividualPacketManager extends PacketManager {

  protected int id;
  protected Socket socket;
  protected DataInputStream in;
  protected DataOutputStream out;
  private ServerNetworkManager server;

  private ExecutorService executor;

  public IndividualPacketManager(int id, Socket socket, ServerNetworkManager server) throws IOException {
    this.id = id;
    this.socket = socket;
    this.in = new DataInputStream(socket.getInputStream());
    this.out = new DataOutputStream(socket.getOutputStream());
    this.server = server;

    sendPacket(new SetPlayerIdPacket(id));

    this.executor = Executors.newSingleThreadExecutor();

    registerPacket(UpdatePosPacket.class);
    registerPacket(JoinRequestPacket.class);
    registerPacket(TeamSelectionPacket.class);
    registerPacket(ReadyUpPacket.class);
  }

  public DataOutputStream getOutputStream() {
    return this.out;
  }

  @Override
  public void onReceive(Packet packet) {
    server.onReceive(packet, id);
  }

  public void sendPacket(Packet packet) {
    try {
      super.sendPacket(packet, out);
    } catch (IOException e) {
      System.out.println("[server:network] Failed to sendPacket " + packet.getId() + ", disconnecting: " + e.getMessage());
      disconnect();
    }
  }

  @Override
  public void disconnect() {
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    server.removeConnection(this);
    executor.shutdownNow();
  }

  public void start() {
    executor.submit(() -> {
      try {
        while (true) {
          Packet packet = receivePacket(in);
          onReceive(packet);
        }
      } catch (IOException e) {
        System.out.println("[server:network] Failed to receive packet, disconnecting: " + e.getMessage());
        disconnect();
      }
    });
  }
}
