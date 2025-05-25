package client;

import java.awt.Graphics;

import client.view.ClientScreen;
import game.Game;
import model.Player;
import model.Team;
import network.Packet;
import network.packets.JoinRequestPacket;
import network.packets.ReadyUpPacket;
import network.packets.TeamSelectionPacket;

public class ClientController {

  // GameState Enum
  public enum GameState {
    MENU,
    LOBBY,
    IN_GAME
  }
  
  private ClientPacketManager packetManager;
  private ClientScreen screen;
  
  public int id;
  private Game game;

  public ClientController(ClientPacketManager packetManager, ClientScreen screen) {
    this.packetManager = packetManager;
    this.screen = screen;

    this.packetManager.setController(this);
    this.screen.setController(this);

    this.game = new Game();
    game.initPlayer();
  }

  public void setId(int id) {
    this.id = id;
    this.game.setId(id);
    System.out.println("[client:controller] Set ID: " + id);
  }

  public void selectTeam(int playerId, String team) {
    if (playerId == id) {
      System.out.println("[client:controller] Setting own team to: " + team);
      game.getPlayer().team = Team.valueOf(team);
    } else {
      game.updatePlayerTeam(playerId, Team.valueOf(team));
    }
  }
  
  public void requestTeam(String team) {
    sendPacket(new TeamSelectionPacket(team));
    game.getPlayer().team = Team.valueOf(team);
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
        screen.showScreen("lobbyScreen");
        break;
      case IN_GAME:
        startGame();
        screen.showScreen("gameScreen");
        break;
    }
  }

  public void startGame() {
    game.start();
    packetManager.startSending(game);
  }

  public void addPlayer(int playerId, Player player) {
    System.out.println("[client:controller] Adding player " + playerId);
    game.addPlayer(playerId, player);
  }

  public void updatePlayerPosition(int playerId, float x, float y) {
    System.out.println("[client:controller] Updating position for player " + playerId + " to (" + x + ", " + y + ")");
    game.updatePlayerPosition(playerId, x, y);
  }

  public void stopGame() {
    game.stop();
  }

  public void drawGame(Graphics g) {
    game.draw(g);
  }

  public void attemptConnect() {
    connect("localhost", 31415);
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
