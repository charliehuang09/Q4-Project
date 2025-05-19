package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Packet;
import network.packets.UpdatePosPacket;
import network.packets.TeamSelectionPacket;
import network.packets.ReadyUpPacket;
import struct.MyArrayList;
import network.packets.JoinRequestPacket;

public class ServerNetworkManager {
  public static final int MAX_CONNECTIONS = 4;

  private ServerSocket serverSocket;
  private ExecutorService executor;
  private int lastConnectionId = 0;
  private MyArrayList<IndividualPacketManager> connectionManagers;
  private MyArrayList<String> clientIps;

  private ServerController controller;

  public ServerNetworkManager() {
    executor = Executors.newFixedThreadPool(MAX_CONNECTIONS);
    connectionManagers = new MyArrayList<>();
    clientIps = new MyArrayList<>();
  }

  public void setController(ServerController controller) {
    this.controller = controller;
  }

  public void broadcast(Packet packet) {
    connectionManagers.forEach(ipm -> ipm.sendPacket(packet));
  }

  public void onReceive(Packet packet, int playerId) {
    System.out.println("[server:network] Received " + packet.getId());

    if (packet instanceof JoinRequestPacket jrp) {
      System.out.println("[server:network] Client joined: " + jrp.clientName);
    } else if (packet instanceof TeamSelectionPacket tsp) {
      System.out.println("[server:network] Team selected: " + tsp.getTeam());

      controller.handleTeamSelection(playerId, tsp.getTeam());
    } else if (packet instanceof ReadyUpPacket rup) {
      System.out.println("[server:network] Ready status: " + rup.isReady());

      controller.handleReadyStatus(playerId, rup.isReady());
    } else if (packet instanceof @SuppressWarnings("unused") UpdatePosPacket upp) {
      
    }
  }

  public void disconnect() {
    for (IndividualPacketManager ipm : connectionManagers) {
      ipm.disconnect();
    }
  }

  public void removeConnection(IndividualPacketManager ipm) {
    System.out.println("[server:network] Client disconnected: " + ipm.socket.getInetAddress().getHostAddress());
    clientIps.remove(ipm.socket.getInetAddress().getHostAddress());
    controller.updateIPs(clientIps);
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
          controller.updateIPs(clientIps);

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
