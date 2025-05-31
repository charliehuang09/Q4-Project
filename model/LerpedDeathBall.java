package model;

import java.awt.Color;
import java.awt.Graphics;

import util.Util;

public class LerpedDeathBall extends DeathBall {
  public float previousX, previousY;
  public float lerpedX, lerpedY;
  public long lastUpdateTime;

  public LerpedDeathBall(float x, float y) {
    super(x, y);
  }

  public static LerpedDeathBall fromDeathBall(DeathBall deathBall) {
    LerpedDeathBall lerpedDeathBall = new LerpedDeathBall(deathBall.body.state.x, deathBall.body.state.y);
    lerpedDeathBall.body.state.x_vel = deathBall.body.state.x_vel;
    lerpedDeathBall.body.state.y_vel = deathBall.body.state.y_vel;
    return lerpedDeathBall;
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
    // Calculate lerped position
    long currentTime = System.currentTimeMillis();
    float alpha = (currentTime - lastUpdateTime) / (1000f / 20);
    lerpedX = Util.lerp(previousX, body.state.x, alpha);
    lerpedY = Util.lerp(previousY, body.state.y, alpha);

    g.setColor(new Color(0, 0, 0, 100)); // Semi-transparent background
    g.fillOval(
        (int) (lerpedX - RADIUS + 1),
        (int) (lerpedY - RADIUS + 1),
        (int) (RADIUS * 2 + 1),
        (int) (RADIUS * 2 + 1));

    g.setColor(Color.RED);
    g.fillOval(
        (int) (lerpedX - RADIUS),
        (int) (lerpedY - RADIUS),
        (int) (RADIUS * 2),
        (int) (RADIUS * 2));
    g.setColor(Color.WHITE);
    g.fillOval(
        (int) (lerpedX - RADIUS + 3),
        (int) (lerpedY - RADIUS + 3),
        (int) (RADIUS * 2 - 6),
        (int) (RADIUS * 2 - 6));
  }
}
