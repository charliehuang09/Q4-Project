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

  public GameOverScreen gameOverScreen; // for controller to access

  private ClientController controller;

  private ScheduledExecutorService executor;

  public ClientScreen() {
    JFrame frame = new JFrame("Client Screen");
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    // Start Screen
    MenuScreen menuScreen = new MenuScreen(this);
    LobbyScreen lobbyScreen = new LobbyScreen(this);
    GameScreen gameScreen = new GameScreen(this);
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

    this.executor = Executors.newSingleThreadScheduledExecutor();
    this.executor.scheduleAtFixedRate(mainPanel::repaint, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  public void setController(ClientController controller) {
    this.controller = controller;
  }

  public ClientController getController() {
    return controller;
  }

  public CardLayout getCardLayout() {
    return cardLayout;
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
