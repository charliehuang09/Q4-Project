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
import model.Team;
import struct.MyHashMap;
import network.packets.SwitchStatePacket;

public class ServerNetworkManager {
  public static final int MAX_CONNECTIONS = 4;

  private ServerSocket serverSocket;
  private ExecutorService executor;
  private int lastConnectionId = 0;
  private MyArrayList<IndividualPacketManager> connectionManagers;
  private MyArrayList<String> clientIps;

  private ServerScreen screen;
  private GameState gameState = GameState.LOBBY;
  private MyHashMap<Integer, Team> playerTeams = new MyHashMap<>();
  private MyHashMap<Integer, Boolean> playerReadyStatus = new MyHashMap<>();

  public ServerNetworkManager() {
    executor = Executors.newFixedThreadPool(MAX_CONNECTIONS);
    connectionManagers = new MyArrayList<>();
    clientIps = new MyArrayList<>();
  }

  public void setScreen(ServerScreen screen) {
    this.screen = screen;
  }

  public void broadcast(Packet packet) {
    connectionManagers.forEach(ipm -> ipm.sendPacket(packet));
  }

  private enum GameState {
    LOBBY,
    IN_GAME
  }

  public void onReceive(Packet packet, int playerId) {
    System.out.println("[server:network] Received " + packet.getId());

    if (packet instanceof JoinRequestPacket jrp) {
      System.out.println("[server:network] Client joined: " + jrp.clientName);
    } else if (packet instanceof TeamSelectionPacket tsp) {
      System.out.println("[server:network] Team selected: " + tsp.getTeam());

      if (gameState == GameState.LOBBY) {
        Team team = Team.valueOf(tsp.getTeam().toUpperCase());
        playerTeams.put(playerId, team);
        System.out.println("[server:network] Player " + playerId + " selected team " + tsp.getTeam());
      }
    } else if (packet instanceof ReadyUpPacket rup) {
      System.out.println("[server:network] Ready status: " + rup.isReady());

      if (gameState == GameState.LOBBY) {
        playerReadyStatus.put(playerId, rup.isReady());
        System.out.println("[server:network] Player " + playerId + " is ready: " + rup.isReady());

        boolean allReady = true;
        for (Integer key : playerReadyStatus.keySet()) {
          if (!playerReadyStatus.get(key)) {
            allReady = false;
            break;
          }
        }

        if (allReady) {
          gameState = GameState.IN_GAME;
          
          // Broadcast SwitchStatePacket to all clients
          SwitchStatePacket switchStatePacket = new SwitchStatePacket("IN_GAME");
          broadcast(switchStatePacket);

          System.out.println("[server:network] All players are ready. Starting the game!");
        }
      }
    } else if (packet instanceof UpdatePosPacket upp) {
      
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
    screen.updateIPs(clientIps);
    connectionManagers.remove(ipm);
  }

  public void start(int port) throws IOException {
    executor.submit(() -> {
      try {
        serverSocket = new ServerSocket(port);

        gameState = GameState.LOBBY;
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
