package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Packet;
import network.PacketManager;
import network.packets.UpdatePosPacket;
import struct.MyArrayList;
import network.packets.JoinRequestPacket;

public class ServerPacketManager extends PacketManager {
  public static final int MAX_CONNECTIONS = 4;

  private ServerSocket serverSocket;
  private ExecutorService executor;
  private int lastConnectionId = 0;
  private MyArrayList<IndividualPacketManager> connectionManagers;
  private MyArrayList<String> clientIps;

  private ServerScreen screen;

  public ServerPacketManager() {
    executor = Executors.newFixedThreadPool(MAX_CONNECTIONS);
    connectionManagers = new MyArrayList<>();
    clientIps = new MyArrayList<>();
  }

  public void setScreen(ServerScreen screen) {
    this.screen = screen;
  }

  @Override
  public void onReceive(Packet packet) {
    System.out.println("[server:network] Received " + packet.getId());
    if (packet instanceof JoinRequestPacket jrp) {
      System.out.println("[server:network] Client joined: " + jrp.clientName);
    } else if (packet instanceof UpdatePosPacket upp) {
      screen.setMousePos((int) upp.x, (int) upp.y);
    }
  }

  @Override
  public void disconnect() {
    for (IndividualPacketManager ipm : connectionManagers) {
      ipm.disconnect();
    }
  }

  public void removeConnection(IndividualPacketManager ipm) {
    System.out.println("[server:network] Client disconnected: " + ipm.socket.getInetAddress().getHostAddress());
    clientIps.remove(ipm.socket.getInetAddress().getHostAddress());
    screen.updateIPs(clientIps);
    connectionManagers.remove(ipm);
  }

  public void start(int port) throws IOException {
    executor.submit(() -> {
      try {
        serverSocket = new ServerSocket(port);
  
        while (true) {
          Socket newConnection = serverSocket.accept();
          IndividualPacketManager ipm = new IndividualPacketManager(lastConnectionId++, newConnection, this);
          System.out.println("[server:network] Client connected: " + newConnection.getInetAddress().getHostAddress());
          clientIps.add(newConnection.getInetAddress().getHostAddress());
          screen.updateIPs(clientIps);

          ipm.start();
          connectionManagers.add(ipm);
        }
      } catch (IOException e) {
        System.out.println("[server:network] ServerSocket error: " + e.getMessage());
        disconnect();
      }
    });
  }
}
