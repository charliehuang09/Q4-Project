package model;

import java.awt.Color;
import java.awt.Graphics;

import util.Util;

public class LerpedPlayer extends Player {
  public float previousX, previousY;
  public float lerpedX, lerpedY;
  public long lastUpdateTime;

  public LerpedPlayer(float x, float y, String name, Team team, DeathBall deathBall) {
    super(x, y, name, team, deathBall);
  }

  public static LerpedPlayer fromPlayer(Player player) {
    LerpedPlayer lerpedPlayer = new LerpedPlayer(player.body.state.x, player.body.state.y, player.name, player.team, player.graple.deathBall);
    lerpedPlayer.body.state.x_vel = player.body.state.x_vel;
    lerpedPlayer.body.state.y_vel = player.body.state.y_vel;
    lerpedPlayer.alive = player.alive;
    lerpedPlayer.graple.active = player.graple.active;
    return lerpedPlayer;
  }

  @Override
  public void setPosition(float x, float y) {
    this.previousX = this.body.state.x;
    this.previousY = this.body.state.y;
    this.lastUpdateTime = System.currentTimeMillis();
    super.setPosition(x, y);
  }

  @Override
  public void draw(Graphics g) {
    if (!alive)
      return;

    // Calculate lerped position
    long currentTime = System.currentTimeMillis();
    float alpha = (currentTime - lastUpdateTime) / (1000f / 20);
    lerpedX = Util.lerp(previousX, body.state.x, alpha);
    lerpedY = Util.lerp(previousY, body.state.y, alpha);

    graple.draw(g);

    g.setColor(new Color(0, 0, 0, 100)); // Semi-transparent background
    g.fillOval(
        (int) (lerpedX - RADIUS + 1),
        (int) (lerpedY - RADIUS + 1),
        (int) (RADIUS * 2 + 1),
        (int) (RADIUS * 2 + 1));

    if (team == Team.RED) {
      g.setColor(Color.RED);
    } else {
      g.setColor(Color.BLUE);
    }
    g.fillOval(
        (int) (lerpedX - RADIUS),
        (int) (lerpedY - RADIUS),
        (int) (RADIUS * 2),
        (int) (RADIUS * 2));
  }
}
