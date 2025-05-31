package model;

import java.awt.Color;
import java.awt.Graphics;

public class Graple {
  private float MAX_DISTANCE = 100.0f;
  public float grapleLength;
  public DeathBall deathBall;
  public Player player;
  public boolean active;

  public Graple(Player player, DeathBall deathBall) {
    this.player = player;
    this.deathBall = deathBall;
    this.active = false;
    this.grapleLength = -1f;
  }

  private float getDistance() {
    return (float)
        Math.sqrt(
            (deathBall.body.state.x - player.body.state.x)
                    * (deathBall.body.state.x - player.body.state.x)
                + (deathBall.body.state.y - player.body.state.y)
                    * (deathBall.body.state.y - player.body.state.y));
  }

  private void applyTetherForce(float dt) {
    float distance = getDistance();
    float x = deathBall.body.state.x - player.body.state.x;
    float y = deathBall.body.state.y - player.body.state.y;
    float ux = x / distance;
    float uy = y / distance;
    float tx = ux * grapleLength;
    float ty = uy * grapleLength;
    float vx = x - tx;
    float vy = y - ty;
    deathBall.applyForce(-vx * 5.0f * dt, -vy * 5.0f * dt);
  }

  public void update(float dt, boolean on) {
    if (!on) {
      active = false;
      return;
    }

    if (!active) {
      if (getDistance() < MAX_DISTANCE) {
        active = true;
        grapleLength = getDistance();
      }
    }
    if (active) {
      if (getDistance() > grapleLength) {
        applyTetherForce(dt);
      }
    }
  }

  public void draw(Graphics g) {
    if (!active) return;
    g.setColor(Color.BLACK);
    g.drawLine(
        (int) (player.body.state.x),
        (int) (player.body.state.y),
        (int) (deathBall.body.state.x),
        (int) (deathBall.body.state.y));
  }
}
