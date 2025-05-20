package client.view;

import game.Game;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class GameScreen extends JPanel {
  @SuppressWarnings("unused")
  private ClientScreen clientScreen;

  private Game game;

  public GameScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;

    game = new Game();

    this.setPreferredSize(new Dimension(1000, 450));
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(this::update, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  private void update() {
    game.update(1.0f);
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    game.draw(g);
  }
}
