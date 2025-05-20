package client.view;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class GameScreen extends JPanel {
  private ClientScreen clientScreen;

  private ScheduledExecutorService executor;

  public GameScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;

    this.setPreferredSize(new Dimension(1000, 450));
    executor = Executors.newScheduledThreadPool(1);

    executor.scheduleAtFixedRate(this::repaint, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    clientScreen.getController().drawGame(g);
  }
}
