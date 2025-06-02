package client;

import client.view.ClientScreen;
import java.awt.Graphics;
import model.Player;
import model.Team;
import network.Packet;
import network.packets.*;
import util.Util;

public class ClientController {
  public static final boolean DEBUG = false;

  static void dprintln(String message) {
    if (DEBUG) System.out.println(message);
  }

  // GameState Enum
  public enum GameState {
    MENU,
    LOBBY,
    IN_GAME,
    GAME_OVER
  }

  private ClientPacketManager packetManager;
  private ClientScreen screen;

  public int id;
  private ClientGame game;
  public boolean[] keys = new boolean[5];

  public ClientController(ClientPacketManager packetManager, ClientScreen screen) {
    this.packetManager = packetManager;
    this.screen = screen;

    this.packetManager.setController(this);
    this.screen.setController(this);

    this.game = new ClientGame(this);
  }

  public void setId(int id) {
    this.id = id;
    System.out.println("[client:controller] Set ID: " + id);
  }

  public void selectTeam(int playerId, String team) {
    if (playerId == id) {
      System.out.println("[client:controller] Setting own team to: " + team);
      game.player.team = Team.valueOf(team);
    } else {
      game.updatePlayerTeam(playerId, Team.valueOf(team));
    }
  }

  public void requestTeam(String team) {
    sendPacket(new TeamSelectionPacket(team));
    game.player.team = Team.valueOf(team);
  }

  public void readyUp(boolean ready) {
    sendPacket(new ReadyUpPacket(ready));
  }

  public void setCurrentState(GameState state) {
    System.out.println("[client:controller] Changing state to " + state);
    switch (state) {
      case MENU:
        screen.showScreen("menuScreen");
        break;
      case LOBBY:
        stopGame();
        screen.showScreen("lobbyScreen");
        break;
      case IN_GAME:
        startGame();
        screen.showScreen("gameScreen");
        break;
      case GAME_OVER:
        stopGame();
        screen.updateScore(game.blueScore, game.redScore);
        Util.playSound("victory.wav");
        screen.showScreen("gameOverScreen");
        break;
    }
  }

  public void startGame() {
    System.out.println("[client:controller] Starting game");
    screen.toggleReady(); // unready before starting a new game
    game.start();
    packetManager.startSending(game);
  }

  public void stopGame() {
    System.out.println("[client:controller] Stopping game");
    packetManager.stopSending();
    game.stop();
    screen.resetKeys();
  }

  public void resetGame() {
    System.out.println("[client:controller] Resetting game");
    game.reset();
  }

  public void onCheatKeyPressed() {
    System.out.println("[client:controller] Cheat key pressed");
    sendPacket(new CheatKeyPacket());
  }

  public void addPlayer(int playerId, Player player) {
    System.out.println("[client:controller] Adding player " + playerId + " - " + player.name);
    game.addPlayer(playerId, player);
  }

  public void addPlayer(int playerId, String name, String team) {
    Player player = new Player(0, 0, name, Team.valueOf(team), game.deathBall);
    addPlayer(playerId, player);
  }

  public void removePlayer(int playerId) {
    System.out.println("[client:controller] Removing player " + playerId);
    game.removePlayer(playerId);
  }

  public void updatePlayer(
      int playerId, float x, float y, boolean tethering, float tetherLength, boolean alive) {
    if (playerId == id) {
      return; // Don't update own position from server packets
    }

    dprintln(
        "[client:controller] Updating position for player "
            + playerId
            + " to ("
            + x
            + ", "
            + y
            + ")");
    game.updatePlayerPosition(playerId, x, y);
    game.updatePlayerTethering(playerId, tethering, tetherLength);
    game.updatePlayerAlive(playerId, alive);
  }

  public void updateDeathBall(float x, float y, float x_vel, float y_vel) {
    dprintln(
        "[client:controller] Updating death ball position to ("
            + x
            + ", "
            + y
            + ") with velocity ("
            + x_vel
            + ", "
            + y_vel
            + ")");
    game.updateDeathBall(x, y, x_vel, y_vel);
  }

  public void setScore(int blueScore, int redScore) {
    System.out.println(
        "[client:controller] Setting score - Blue: " + blueScore + ", Red: " + redScore);
    game.setScore(blueScore, redScore);
  }

  public void drawGame(Graphics g) {
    game.draw(g);
  }

  public void attemptConnect() {
    connect("10.210.124.243", 31415);
    sendPacket(new JoinRequestPacket("PlayerName")); // TODO: get player name from input
  }

  public void connect(String host, int port) {
    try {
      packetManager.connect(host, port);
      setCurrentState(GameState.LOBBY);
    } catch (Exception e) {
      System.out.println("[client:controller] Can't connect to server: " + e.getMessage());
    }
  }

  public void disconnect() {
    packetManager.disconnect();
    setCurrentState(GameState.MENU);
  }

  public void sendPacket(Packet packet) {
    packetManager.sendPacket(packet);
  }
}
