package client;

import java.awt.Graphics;

import client.view.ClientScreen;
import game.Game;
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

  private Game game;

  public ClientController(ClientPacketManager packetManager, ClientScreen screen) {
    this.packetManager = packetManager;
    this.screen = screen;

    this.packetManager.setController(this);
    this.screen.setController(this);

    this.game = new Game();
  }

  public void selectTeam(String team) {
    System.out.println("[client:controller] Team selected: " + team);
    // TODO: store team
  }
  
  public void requestTeam(String team) {
    // TODO: store team
    sendPacket(new TeamSelectionPacket(team));
  }

  public void readyUp(boolean ready) {
    sendPacket(new ReadyUpPacket(ready));
  }

  public void setCurrentState(GameState state) {
    switch (state) {
      case MENU:
        screen.showScreen("menuScreen");
        break;
      case LOBBY:
        screen.showScreen("lobbyScreen");
        break;
      case IN_GAME:
        screen.showScreen("gameScreen");
        startGame();
        break;
    }
  }

  public void startGame() {
    game.start();
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
    setCurrentState(ClientController.GameState.LOBBY);
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
