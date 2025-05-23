package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.Game;
import model.Player;
import network.Packet;
import network.packets.UpdatePosPacket;
import network.packets.TeamSelectionPacket;
import network.packets.ReadyUpPacket;
import struct.MyArrayList;
import network.packets.JoinRequestPacket;

public class ServerNetworkManager {
  public static final int MAX_CONNECTIONS = 4;

  private ServerSocket serverSocket;
  private ExecutorService receivingExecutor;
  private ScheduledExecutorService sendingExecutor;
  private int lastConnectionId = 0;
  private MyArrayList<IndividualPacketManager> connectionManagers;
  private MyArrayList<String> clientIps;

  private ServerController controller;

  public ServerNetworkManager() {
    receivingExecutor = Executors.newFixedThreadPool(MAX_CONNECTIONS);
    sendingExecutor = Executors.newScheduledThreadPool(1);
    connectionManagers = new MyArrayList<>();
    clientIps = new MyArrayList<>();
  }

  public void setController(ServerController controller) {
    this.controller = controller;
  }

  public void sendPacket(Packet packet, int playerId) {
    System.out.println("[server:network] Sending " + packet.getId() + " to client " + playerId);
    for (IndividualPacketManager ipm : connectionManagers) {
      if (ipm.id == playerId) {
        ipm.sendPacket(packet);
        return;
      }
    }
    System.out.println("[server:network] Client not found: " + playerId);
  }

  public void broadcast(Packet packet) {
    connectionManagers.forEach(ipm -> ipm.sendPacket(packet));
  }

  public void onReceive(Packet packet, int playerId) {
    System.out.println("[server:network] Received " + packet.getId());

    if (packet instanceof JoinRequestPacket jrp) {
      controller.handleJoinRequest(playerId, jrp.clientName);
    } else if (packet instanceof TeamSelectionPacket tsp) {
      controller.handleTeamSelection(playerId, tsp.team);
    } else if (packet instanceof ReadyUpPacket rup) {
      controller.handleReadyStatus(playerId, rup.isReady);
    } else if (packet instanceof UpdatePosPacket upp) {
      controller.updatePlayerPosition(upp.playerId, upp.x, upp.y);
    } else {
      System.out.println("[server:network] Unknown packet type: " + packet.getId());
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
    controller.onDisconnect(ipm.id);
    controller.updateIPs(clientIps);
    connectionManagers.remove(ipm);
  }

  public void startReceiving(int port) throws IOException {
    receivingExecutor.submit(() -> {
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

  public void startSending(Game game) {
    sendingExecutor.scheduleAtFixedRate(() -> {
      if (controller.getGameState() != ServerController.GameState.IN_GAME) {
        return;
      }
      for (Integer id : game.players.keySet()) {
        Player player = game.players.get(id);
        System.out.println("[server:network] Sending position update for player " + id + ": " + player.body.state.x + ", " + player.body.state.y);
        UpdatePosPacket upp = new UpdatePosPacket(id, player.body.state.x, player.body.state.y);
        broadcast(upp);
      }
    }, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }
}
