package client.view;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.*;

import game.Game;

public class GameScreen extends JPanel {
  @SuppressWarnings("unused") private ClientScreen clientScreen;

  private Game game;

  public GameScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;

    game = new Game();

    this.setPreferredSize(new Dimension(1000, 450));
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(this::update, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  private void update() {
    game.update();
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    game.draw(g);
  }
}
