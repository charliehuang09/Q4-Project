package server;

import java.io.IOException;

import model.Team;
import model.Player;
import network.packets.AddPlayerPacket;
import network.packets.SwitchStatePacket;
import struct.MyArrayList;
import struct.MyHashMap;

import game.Game;

public class ServerController {
  private Game game;

  private ServerNetworkManager networkManager;
  private ServerScreen screen;

  public enum GameState {
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

    this.game = new Game();
    this.networkManager.setController(this);
    this.screen.setController(this);

    try {
      networkManager.startReceiving(31415);
      networkManager.startSending(game);
    } catch (IOException e) {
      throw new RuntimeException("Failed to start server");
    }
  }

  public void handleTeamSelection(int playerId, String teamName) {
    if (gameState == GameState.LOBBY) {
      Team team = Team.valueOf(teamName.toUpperCase());
      playerInfos.get(playerId).team = team;
      game.updatePlayerTeam(playerId, team);

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

  public void updatePlayerPosition(int playerId, float x, float y) {
    if (gameState == GameState.IN_GAME) {
      game.updatePlayerPosition(playerId, x, y);
      System.out.println("[server:controller] Player " + playerId + " position updated to: " + x + ", " + y);
    }
  }

  public void handleJoinRequest(int playerId, String clientName) {
    System.out.println("[server:controller] Player " + playerId + " requested to join with name: " + clientName);

    // Broadcast player position updates
    playerInfos.put(playerId, new PlayerInfo(clientName, Team.NONE, false));
    
    // Add new placeholder player to the server's game
    game.addPlayer(playerId, new Player(0, 0, Team.NONE));

    // Send the client all of the other players, and send the new player to all of the other clients
    for (Integer otherPlayerId : playerInfos.keySet()) {
      if (otherPlayerId == playerId) {
        continue; // Skip sending the player to themselves
      }

      PlayerInfo playerInfo = playerInfos.get(otherPlayerId);
      networkManager.sendPacket(new AddPlayerPacket(otherPlayerId, playerInfo.name, playerInfo.team.toString()), playerId);
      networkManager.sendPacket(new AddPlayerPacket(playerId, clientName, "NONE"), otherPlayerId);
    }

    System.out.println("[server:controller] Player " + playerId + " successfully joined the game.");
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
