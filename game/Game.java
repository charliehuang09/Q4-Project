package game;

import java.awt.Graphics;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.Platform;
import model.Player;
import model.Sprite;
import model.Team;
import struct.MyArrayList;

public class Game {
  Sprite player;
  MyArrayList<Sprite> sprites;

  private ScheduledExecutorService executor;
  private long time;

  public Game() {
    initGame();

    executor = Executors.newScheduledThreadPool(1);
  }

  private void initGame() {
    this.player = new Player(100, 10, Team.BLUE);
    this.sprites = new MyArrayList<Sprite>();
    this.sprites.add(new Platform(100, 200, 1000, 50)); // TODO: remove this later
  }

  public void start() {
    executor.scheduleAtFixedRate(this::update, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  public void stop() {
    executor.shutdown();
  }

  public void update() {
    if (time == 0) {
      time = System.currentTimeMillis();
      return;
    }

    long ct = System.currentTimeMillis();
    float dt = (float) (ct - time) / 1000f;
    time = ct;

    player.update(sprites, dt);
  }

  public void draw(Graphics g) {
    for (Sprite sprite : sprites) {
      sprite.draw(g);
    }

    player.draw(g);
  }
}
