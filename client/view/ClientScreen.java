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

  public ClientScreen() {
    JFrame frame = new JFrame("Client");
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    // Start Screen
    StartScreen startScreen = new StartScreen(this);
    GameScreen gameScreen = new GameScreen(this);

    mainPanel.add(startScreen, "startScreen");
    mainPanel.add(gameScreen, "gameScreen");
    frame.add(mainPanel);

    showScreen("startScreen");
    frame.pack();
    frame.setVisible(true);

    this.executor = Executors.newSingleThreadScheduledExecutor();
    this.executor.scheduleAtFixedRate(mainPanel::repaint, 0, 1000 / 20, TimeUnit.MILLISECONDS);
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

  public Dimension getPreferredSize() {
    return new Dimension(1000, 450);
  }

  @Override
  public void actionPerformed(ActionEvent e) {}
}
