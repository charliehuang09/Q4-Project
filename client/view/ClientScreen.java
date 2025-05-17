package client.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import client.ClientPacketManager;

public class ClientScreen extends JPanel implements ActionListener {
  private ClientPacketManager pm;
  private JPanel mainPanel;
  private CardLayout cardLayout;

  private ScheduledExecutorService executor;

  // GameState Enum
  public enum GameState {
    MENU,
    LOBBY,
    IN_GAME
  }

  private GameState currentState;

  public ClientScreen() {
    JFrame frame = new JFrame("Client");
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    // Start Screen
    MenuScreen menuScreen = new MenuScreen(this);
    LobbyScreen lobbyScreen = new LobbyScreen(this);
    GameScreen gameScreen = new GameScreen(this);

    mainPanel.add(menuScreen, "menuScreen");
    mainPanel.add(lobbyScreen, "lobbyScreen");
    mainPanel.add(gameScreen, "gameScreen");
    frame.add(mainPanel);

    showScreen("menuScreen"); // Initial screen remains the same
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    this.executor = Executors.newSingleThreadScheduledExecutor();
    this.executor.scheduleAtFixedRate(mainPanel::repaint, 0, 1000 / 60, TimeUnit.MILLISECONDS);

    // Initialize GameState
    this.currentState = GameState.MENU;
  }

  public void setPacketManager(ClientPacketManager pm) {
    this.pm = pm;
  }

  public CardLayout getCardLayout() {
    return cardLayout;
  }

  public void showScreen(String screen) {
    cardLayout.show(mainPanel, screen);
  }

  public ClientPacketManager getPacketManager() {
    return pm;
  }

  public GameState getCurrentState() {
    return currentState;
  }

  public void setCurrentState(GameState state) {
    this.currentState = state;
    switch (state) {
      case MENU:
        showScreen("menuScreen");
        break;
      case LOBBY:
        showScreen("lobbyScreen");
        break;
      case IN_GAME:
        showScreen("gameScreen");
        break;
    }
  }

  public Dimension getPreferredSize() {
    return new Dimension(1000, 450);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
  }
}
