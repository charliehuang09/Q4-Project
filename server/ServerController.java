package server;

import java.io.IOException;

import model.Team;
import network.packets.SwitchStatePacket;
import struct.MyArrayList;
import struct.MyHashMap;

public class ServerController {

  private ServerNetworkManager networkManager;
  private ServerScreen screen;

  private enum GameState {
    LOBBY,
    IN_GAME
  }

  class PlayerInfo {
    String name;
    Team team;
    boolean isReady;

    PlayerInfo(String name, Team team, boolean isReady) {
      this.name = name;
      this.team = team;
      this.isReady = isReady;
    }
  }

  private GameState gameState = GameState.LOBBY;
  private MyHashMap<Integer, PlayerInfo> playerInfos = new MyHashMap<>();

  public ServerController(ServerNetworkManager networkManager, ServerScreen screen) {
    this.networkManager = networkManager;
    this.screen = screen;

    this.networkManager.setController(this);
    this.screen.setController(this);

    try {
      networkManager.start(31415);
    } catch (IOException e) {
      throw new RuntimeException("Failed to start server");
    }
  }

  public void handleTeamSelection(int playerId, String teamName) {
    if (gameState == GameState.LOBBY) {
      Team team = Team.valueOf(teamName.toUpperCase());
      playerInfos.get(playerId).team = team;
      System.out.println("[server:controller] Player " + playerId + " selected team " + teamName);
    }
  }

  public void handleReadyStatus(int playerId, boolean isReady) {
    if (gameState == GameState.LOBBY) {
      playerInfos.get(playerId).isReady = isReady;
      System.out.println("[server:controller] Player " + playerId + " is ready: " + isReady);

      boolean allReady = true;
      for (Integer key : playerInfos.keySet()) {
        if (!playerInfos.get(key).isReady) {
          allReady = false;
          break;
        }
      }

      if (allReady) {
        gameState = GameState.IN_GAME;

        // Broadcast SwitchStatePacket to all clients
        SwitchStatePacket switchStatePacket = new SwitchStatePacket("IN_GAME");
        networkManager.broadcast(switchStatePacket);

        System.out.println("[server:controller] All players are ready. Starting the game!");
      }
    }
  }

  public void onJoinRequest(int playerId, String clientName) {
    playerInfos.put(playerId, new PlayerInfo(clientName, null, false)); 
    System.out.println("[server:controller] Player " + playerId + " joined the game.");
  }

  public void onDisconnect(int playerId) {
    playerInfos.remove(playerId);

    if (playerInfos.size() == 0) {
      gameState = GameState.LOBBY;
      System.out.println("[server:controller] All players disconnected. Resetting game state to LOBBY.");
    }

    System.out.println("[server:controller] Player " + playerId + " disconnected.");
  }

  public void updateIPs(MyArrayList<String> ips) {
    screen.updateIPs(ips);
  }

  public GameState getGameState() {
    return gameState;
  }
}
