package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Packet;
import network.PacketManager;
import network.packets.UpdatePosPacket;

public class IndividualPacketManager extends PacketManager {

  private int id;
  protected Socket socket;
  protected DataInputStream in;
  protected DataOutputStream out;
  private ServerPacketManager server;

  private ExecutorService executor;

  public IndividualPacketManager(int id, Socket socket, ServerPacketManager server) throws IOException {
    this.id = id;
    this.socket = socket;
    this.in = new DataInputStream(socket.getInputStream());
    this.out = new DataOutputStream(socket.getOutputStream());
    this.server = server;
    
    this.executor = Executors.newSingleThreadExecutor();
  
    registerPacket(UpdatePosPacket.class);
  }

  @Override
  public void onReceive(Packet packet) {
    server.onReceive(packet);
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
        e.printStackTrace();
        disconnect();
      }
    });
  }
}
