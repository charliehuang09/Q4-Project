package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Packet;
import network.PacketManager;
import network.packets.SwitchStatePacket;
import network.packets.TeamSelectionPacket;

public class ClientPacketManager extends PacketManager {
  protected Socket socket;
  protected DataInputStream in;
  protected DataOutputStream out;

  private ExecutorService executor;

  private ClientController controller;

  public ClientPacketManager() {
    this.executor = Executors.newSingleThreadExecutor();

    registerPacket(SwitchStatePacket.class);
    registerPacket(TeamSelectionPacket.class);
  }

  @Override
  public void onReceive(Packet packet) {
    System.out.println("[client:network] Received " + packet.getId());

    if (controller != null) {
      controller.handlePacket(packet);
    } else {
      System.out.println("[client:network] Controller is not set. Unable to handle packet.");
    }
  }

  public void setController(ClientController controller) {
    this.controller = controller;
  }

  public void sendPacket(Packet packet) {
    try {
      super.sendPacket(packet, out);
    } catch (Exception e) {
      System.out.println("[client:network] Failed to send packet, disconnecting: " + e.getMessage());
      disconnect();
    }
  }

  public void connect(String host, int port) throws IOException {
    System.out.println("[client:network] Attempting to connect to " + host + ":" + port);
    socket = new Socket(host, port);

    in = new DataInputStream(socket.getInputStream());
    out = new DataOutputStream(socket.getOutputStream());

    start();
  }

  public void disconnect() {
    try {
      System.out.println("[client:network] Disconnecting...");
      controller.setCurrentState(ClientController.GameState.MENU);
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
          System.out.println("[client:network] Waiting for packet...");
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
