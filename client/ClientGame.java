package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import game.Game;
import model.LerpedDeathBall;
import model.LerpedPlayer;
import model.Player;
import model.Sprite;
import model.Team;
import util.Util;

public class ClientGame extends Game {
  private final ClientController controller;
  public Player player;

  private long time = 0;

  private BufferedImage background;

  public ClientGame(ClientController controller) {
    super();
    this.controller = controller;
    this.background = Util.load("background.jpg");
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
      player.setPosition(100, 350);
    } else if (player.team == Team.RED) {
      player.setPosition(900, 350);
    }

    player.body.state.x_vel = 0;
    player.body.state.y_vel = 0;

    player.graple.active = false;
  }

  @Override
  public void resetDeathBall() {
    if (deathBall == null) {
      deathBall = new LerpedDeathBall(500, 80);
    } else {
      deathBall.setPosition(500, 80);
      deathBall.body.state.x_vel = 0;
      deathBall.body.state.y_vel = 0;
    }
  }

  @Override
  public void addPlayer(int id, Player player) {
    this.players.put(id, LerpedPlayer.fromPlayer(player));
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
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.drawImage(background, 0, 0, null);

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
    g.setFont(new Font("Bahnschrift", Font.BOLD, 30));
    FontMetrics fm = g.getFontMetrics();
    int width;

    width = fm.stringWidth(String.valueOf(blueScore));
    g.drawString(String.valueOf(blueScore), 480 - width, 50);

    width = fm.stringWidth(String.valueOf(redScore));
    g.drawString(String.valueOf(redScore), 520, 50);
  }
}
