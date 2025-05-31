package client.view;

import client.ClientController;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class ClientScreen extends JPanel implements ActionListener {
  private JPanel mainPanel;
  private CardLayout cardLayout;

  private LobbyScreen lobbyScreen; // for toggleReady to access
  private GameScreen gameScreen; // for resetKeys to access
  private GameOverScreen gameOverScreen; // for updateScore to access

  private ClientController controller;

  private ScheduledExecutorService executor;

  public ClientScreen() {
    JFrame frame = new JFrame("Client Screen");
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    // Start Screen
    MenuScreen menuScreen = new MenuScreen(this);
    lobbyScreen = new LobbyScreen(this);
    gameScreen = new GameScreen(this);
    gameOverScreen = new GameOverScreen(this);

    mainPanel.add(menuScreen, "menuScreen");
    mainPanel.add(lobbyScreen, "lobbyScreen");
    mainPanel.add(gameScreen, "gameScreen");
    mainPanel.add(gameOverScreen, "gameOverScreen");
    frame.add(mainPanel);
    frame.setFocusable(true);

    showScreen("menuScreen"); // Initial screen remains the same
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
    frame.setResizable(false);

    this.executor = Executors.newSingleThreadScheduledExecutor();
    this.executor.scheduleAtFixedRate(mainPanel::repaint, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  public void setController(ClientController controller) {
    this.controller = controller;
  }

  public ClientController getController() {
    return controller;
  }

  public void toggleReady() {
    lobbyScreen.toggleReady();
  }

  public void updateScore(int blueScore, int redScore) {
    gameOverScreen.updateScore(blueScore, redScore);
  }

  public void resetKeys() {
    gameScreen.resetKeys();
  }

  public void showScreen(String screen) {
    cardLayout.show(mainPanel, screen);
  }

  public Dimension getPreferredSize() {
    return new Dimension(1000, 450);
  }

  @Override
  public void actionPerformed(ActionEvent e) {}
}
