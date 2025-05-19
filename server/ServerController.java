package server;

import java.io.IOException;

import model.Team;
import network.packets.SwitchStatePacket;
import struct.MyArrayList;
import struct.MyHashMap;

public class ServerController {

  private ServerNetworkManager networkManager;
  private ServerScreen screen;

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

  private enum GameState {
    LOBBY,
    IN_GAME
  }

  private GameState gameState = GameState.LOBBY;
  private MyHashMap<Integer, Team> playerTeams = new MyHashMap<>();
  private MyHashMap<Integer, Boolean> playerReadyStatus = new MyHashMap<>();

  public void handleTeamSelection(int playerId, String teamName) {
    if (gameState == GameState.LOBBY) {
      Team team = Team.valueOf(teamName.toUpperCase());
      playerTeams.put(playerId, team);
      System.out.println("[server:controller] Player " + playerId + " selected team " + teamName);
    }
  }

  public void handleReadyStatus(int playerId, boolean isReady) {
    if (gameState == GameState.LOBBY) {
      playerReadyStatus.put(playerId, isReady);
      System.out.println("[server:controller] Player " + playerId + " is ready: " + isReady);

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
        networkManager.broadcast(switchStatePacket);

        System.out.println("[server:controller] All players are ready. Starting the game!");
      }
    }
  }

  public void updateIPs(MyArrayList<String> ips) {
    screen.updateIPs(ips);
  }

  public GameState getGameState() {
    return gameState;
  }
}
