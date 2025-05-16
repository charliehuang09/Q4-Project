package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.view.ClientScreen;
import network.Packet;
import network.PacketManager;
import network.packets.UpdatePosPacket;

public class ClientPacketManager extends PacketManager {
  protected Socket socket;
  protected DataInputStream in;
  protected DataOutputStream out;

  private ExecutorService executor;

  private ClientScreen screen;

  public ClientPacketManager() {
    this.executor = Executors.newSingleThreadExecutor();

    registerPacket(UpdatePosPacket.class);
  }

  public void setScreen(ClientScreen screen) {
    this.screen = screen;
  }

  @Override
  public void onReceive(Packet packet) {
    System.out.println("[client:network] Received " + packet.getId());
  }

  public void sendPacket(Packet packet) throws IOException {
    super.sendPacket(packet, out);
  }

  public void connect(String host, int port) throws IOException {
    socket = new Socket(host, port);

    in = new DataInputStream(socket.getInputStream());
    out = new DataOutputStream(socket.getOutputStream());
  }

  public void disconnect() {
    try {
      screen.showScreen("startScreen");
      socket.close();
      executor.shutdownNow();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void start() {
    executor.submit(() -> {
      try {
        while (true) {
          Packet packet = receivePacket(in);
          onReceive(packet);
        }
      } catch (IOException e) {
        System.out.println("[client:network] Connection closed by server: " + e.getMessage());
        disconnect();
      }
    });
  }
}