package server;

import java.io.IOException;

import model.Team;
import model.Player;
import network.packets.AddPlayerPacket;
import network.packets.RemovePlayerPacket;
import network.packets.ResetPacket;
import network.packets.SetScorePacket;
import network.packets.SetTeamPacket;
import network.packets.SwitchStatePacket;
import struct.MyArrayList;
import struct.MyHashMap;

public class ServerController {
  public static final boolean DEBUG = false;

  static void dprintln(String message) {
    if (DEBUG)
      System.out.println(message);
  }

  private ServerGame game;

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

    this.game = new ServerGame(this);
    this.networkManager.setController(this);
    this.screen.setController(this);

    try {
      networkManager.startReceiving(31415);
      networkManager.startSending(game);
    } catch (IOException e) {
      throw new RuntimeException("Failed to start server");
    }
  }

  public void selectTeam(int playerId, String teamName) {
    if (gameState == GameState.LOBBY) {
      Team team = Team.valueOf(teamName.toUpperCase());
      playerInfos.get(playerId).team = team;
      game.updatePlayerTeam(playerId, team);
      networkManager.broadcast(new SetTeamPacket(playerId, teamName));

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
        System.out.println("[server:controller] All players are ready. Starting the game!");
        startGame();
      }
    }
  }

  public void startGame() {
    System.out.println("[server:controller] Starting game...");
    for (Integer playerId : playerInfos.keySet()) {
      PlayerInfo playerInfo = playerInfos.get(playerId);

      if (playerInfo.team == Team.NONE) {
        // Assign a team if not already assigned
        playerInfo.team = (Math.random() > 0.5) ? Team.RED : Team.BLUE;
        game.updatePlayerTeam(playerId, playerInfo.team);
        networkManager.sendPacket(new SetTeamPacket(playerId, playerInfo.team.toString()), playerId);
      }
    }

    game.start();
    gameState = GameState.IN_GAME;

    // Broadcast SwitchStatePacket to all clients
    SwitchStatePacket switchStatePacket = new SwitchStatePacket("IN_GAME");
    networkManager.broadcast(switchStatePacket);
  }

  public void stopGame() {
    System.out.println("[server:controller] Stopping game...");
    gameState = GameState.LOBBY;
    networkManager.broadcast(new SwitchStatePacket("LOBBY"));
    networkManager.stopSending();
    game.stop();
  }

  public void nextRound() {
    System.out.println("[server:controller] Starting next round...");
    game.reset();

    // Broadcast ResetPacket to all clients
    networkManager.broadcast(new ResetPacket());
    networkManager.broadcast(new SetScorePacket(game.blueScore, game.redScore));
    if (game.isGameOver()) {
      System.out.println("[server:controller] Game over!");
      stopGame();
      networkManager.broadcast(new SwitchStatePacket("GAME_OVER"));
    }
  }

  public void updatePlayer(int playerId, float x, float y, boolean tethering, float tetherLength, boolean alive) {
    if (gameState == GameState.IN_GAME) {
      game.updatePlayerPosition(playerId, x, y);
      game.updatePlayerTethering(playerId, tethering, tetherLength);
      game.updatePlayerAlive(playerId, alive);
      dprintln("[server:controller] Player " + playerId + " position updated to: " + x + ", " + y);
    }
  }

  public void handleJoinRequest(int playerId, String clientName) {
    System.out.println("[server:controller] Player " + playerId + " requested to join with name: " + clientName);

    playerInfos.put(playerId, new PlayerInfo(clientName, Team.NONE, false));

    // Add new placeholder player to the server's game
    game.addPlayer(playerId, new Player(0, 0, clientName, Team.NONE, game.deathBall));

    // Send the client all of the other players, and send the new player to all of
    // the other clients
    for (Integer otherPlayerId : playerInfos.keySet()) {
      if (otherPlayerId == playerId) {
        continue; // Skip sending the player to themselves
      }

      PlayerInfo playerInfo = playerInfos.get(otherPlayerId);
      networkManager.sendPacket(new AddPlayerPacket(otherPlayerId, playerInfo.name, playerInfo.team.toString()),
          playerId);
      networkManager.sendPacket(new AddPlayerPacket(playerId, clientName, "NONE"), otherPlayerId);
    }

    System.out.println("[server:controller] Player " + playerId + " successfully joined the game.");
  }

  public boolean checkGameExit() {
    return playerInfos.size() == 0;
  }

  public void onDisconnect(int playerId) {
    playerInfos.remove(playerId);
    game.removePlayer(playerId);
    networkManager.broadcast(new RemovePlayerPacket(playerId));

    if (checkGameExit()) {
      System.out.println("[server:controller] Not enough players left to continue the game. Stopping the game.");
      stopGame();
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
