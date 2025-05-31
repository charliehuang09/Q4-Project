package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import game.Game;
import model.Player;
import model.Sprite;
import model.Team;

public class ClientGame extends Game {
  private final ClientController controller;
  public Player player;

  private long time = 0;

  public ClientGame(ClientController controller) {
    super();
    this.controller = controller;
  }

  public Player getPlayer() {
    return this.player;
  }

  public void initPlayer() {
    this.player = new Player(0, 0, "PlayerName", Team.NONE, deathBall);
  }

  public void resetPlayer() {
    if (player == null) {
      initPlayer();
    }

    player.alive = true;
    if (player.team == Team.BLUE) {
      player.body.state.x = 100;
      player.body.state.y = 400;
    } else if (player.team == Team.RED) {
      player.body.state.x = 900;
      player.body.state.y = 400;
    }

    player.body.state.x_vel = 0;
    player.body.state.y_vel = 0;

    player.graple.active = false;
  }

  @Override
  public void reset() {
    super.reset();
    resetPlayer();
  }

  @Override
  public void start() {
    super.start();
    time = System.currentTimeMillis();
  }

  @Override
  public void update() {
    if (time == 0) {
      time = System.currentTimeMillis();
      return;
    }

    long ct = System.currentTimeMillis();
    float dt = (float) (ct - time) / 1000f;
    time = ct;

    boolean[] keys = controller.keys;

    player.update(sprites, dt, keys);
  }

  public void draw(Graphics g) {
    for (Sprite sprite : sprites) {
      sprite.draw(g);
    }

    for (Player player : players.values()) {
      player.draw(g);
    }

    player.draw(g);
    deathBall.draw(g);

    // Draw game score
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.BOLD, 30));
    FontMetrics fm = g.getFontMetrics();
    int width;
    
    width = fm.stringWidth(String.valueOf(blueScore));
    g.drawString(String.valueOf(blueScore), 480 - width, 50);

    width = fm.stringWidth(String.valueOf(redScore));
    g.drawString(String.valueOf(redScore), 520, 50);
  }
}
