package client;

import client.view.ClientScreen;
import network.Packet;
import network.packets.JoinRequestPacket;
import network.packets.ReadyUpPacket;
import network.packets.SwitchStatePacket;
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

  public ClientController(ClientPacketManager packetManager, ClientScreen screen) {
    this.packetManager = packetManager;
    this.screen = screen;

    this.packetManager.setController(this);
    this.screen.setController(this);
  }

  public void handleSwitchState(SwitchStatePacket packet) {
    if (packet.getNewState().equals("IN_GAME")) {
      setCurrentState(GameState.IN_GAME);
      System.out.println("[client:controller] Switched to IN_GAME state.");
    }
  }

  public void handleTeamSelection(TeamSelectionPacket packet) {
    System.out.println("[client:controller] Team selected: " + packet.getTeam());
    // TODO: store team
  }

  public void attemptConnect() {
    connect("localhost", 31415);
    sendPacket(new JoinRequestPacket("PlayerName")); // TODO: get player name from input
    setCurrentState(ClientController.GameState.LOBBY);
  }

  public void joinTeam(String team) {
    // TODO: store team
    sendPacket(new TeamSelectionPacket(team));
  }

  public void readyUp(boolean ready) {
    sendPacket(new ReadyUpPacket(ready));
  }

  public void connect(String host, int port) {
    try {
      packetManager.connect(host, port);
      setCurrentState(GameState.LOBBY);
    } catch (Exception e) {
      System.out.println("[client:controller] Can't connect to server: " + e.getMessage());
    }
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
        break;
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
