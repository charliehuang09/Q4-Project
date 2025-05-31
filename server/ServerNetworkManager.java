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
import network.packets.UpdatePlayerPacket;
import network.packets.TeamSelectionPacket;
import network.packets.UpdateDeathBallPacket;
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
    connectionManagers = new MyArrayList<>();
    clientIps = new MyArrayList<>();
  }

  public void setController(ServerController controller) {
    this.controller = controller;
  }

  public void sendPacket(Packet packet, int playerId) {
    ServerController.dprintln("[server:network] Sending " + packet.getId() + " to client " + playerId);
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
    ServerController.dprintln("[server:network] Received " + packet.getId());

    if (packet instanceof JoinRequestPacket jrp) {
      controller.handleJoinRequest(playerId, jrp.clientName);
    } else if (packet instanceof TeamSelectionPacket tsp) {
      controller.selectTeam(playerId, tsp.team);
    } else if (packet instanceof ReadyUpPacket rup) {
      controller.handleReadyStatus(playerId, rup.isReady);
    } else if (packet instanceof UpdatePlayerPacket upp) {
      controller.updatePlayer(upp.playerId, upp.x, upp.y, upp.tethering, upp.tetherLength, upp.alive);
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
    connectionManagers.remove(ipm);
    controller.onDisconnect(ipm.id);
    clientIps.remove(ipm.socket.getInetAddress().getHostAddress());
    controller.updateIPs(clientIps);
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
    sendingExecutor = Executors.newScheduledThreadPool(1);
    sendingExecutor.scheduleAtFixedRate(() -> {
      if (controller.getGameState() != ServerController.GameState.IN_GAME) {
        return;
      }

      // Update player position for everyone
      for (Integer id : game.players.keySet()) {
        Player player = game.players.get(id);
        ServerController.dprintln("[server:network] Sending position update for player " + id + ": "
            + player.body.state.x + ", " + player.body.state.y);
        UpdatePlayerPacket upp = new UpdatePlayerPacket(id, player.body.state.x, player.body.state.y,
            player.graple.active, player.graple.grapleLength, player.alive);
        broadcast(upp);
      }

      // Update death ball position
      UpdateDeathBallPacket udp = new UpdateDeathBallPacket(
          game.deathBall.body.state.x,
          game.deathBall.body.state.y,
          game.deathBall.body.state.x_vel,
          game.deathBall.body.state.y_vel);
      broadcast(udp);
    }, 0, 1000 / 20, TimeUnit.MILLISECONDS);
  }

  public void stopSending() {
    sendingExecutor.shutdown();
  }
}
