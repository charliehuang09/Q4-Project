package client.view;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.*;

import model.Player;
import model.Sprite;
import model.Team;
import struct.MyArrayList;
import render.Render;

public class GameScreen extends JPanel {
  private ClientScreen clientScreen;
  private MyArrayList<Sprite> sprites;

  public GameScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;
    
    this.sprites = new MyArrayList<Sprite>();
    this.sprites.add(new Player(100, 100, Team.BLUE));

    this.setPreferredSize(new Dimension(1000, 450));
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(this::update, 0, 50, TimeUnit.MILLISECONDS);
  }

  private void update() {
    for (Sprite sprite : this.sprites) {
      sprite.applyForce(0, 0.1f);
      sprite.applyDrag(0.9f);
    }
    for (Sprite sprite : this.sprites) {
      sprite.update();
    }
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Render.draw(g, sprites);
  }
}
