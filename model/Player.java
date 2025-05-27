package model;

import java.awt.*;
import struct.MyArrayList;
import util.Util;

public class Player extends Sprite {
  static final float RADIUS = 15;

  public String name;
  public Team team;
  public CircleRigid body;
  public Graple graple;

  public Player(float x, float y, String name, Team team, DeathBall deathBall) {
    this.name = name;
    this.team = team;
    this.body = new CircleRigid(x, y, RADIUS);
    this.graple = new Graple(this, deathBall);
  }

  public Player cloneWithOffset(float x_offset, float y_offset) {
    Player newPlayer = new Player(body.state.x, body.state.y, name, team, graple.deathBall);
    newPlayer.body.state.x_vel += x_offset;
    newPlayer.body.state.y_vel += y_offset;
    return newPlayer;
  }

  @Override
  public boolean canMove() {
    return true;
  }

  @Override
  public void draw(Graphics g) {
    if (team == Team.RED) {
      g.setColor(Color.RED);
    } else {
      g.setColor(Color.BLUE);
    }
    g.fillOval(
        (int) (body.state.x - RADIUS),
        (int) (body.state.y - RADIUS),
        (int) (RADIUS * 2),
        (int) (RADIUS * 2));
  }

  @Override
  public void applyForce(float x, float y, float mass) {
    body.state.x_vel += x;
    body.state.y_vel += y;
  }

  @Override
  public void applyForce(float x, float y) {
    applyForce(x, y, body.state.mass);
  }

  @Override
  public void update(MyArrayList<Sprite> sprites, float dt, boolean keys[]) {
    if (keys[0]) {
      applyForce(0, -200f * dt);
    }
    if (keys[1]) {
      applyForce(-200f * dt, 0f);
    }
    if (keys[2]) {
      applyForce(0, 200f * dt);
    }
    if (keys[3]) {
      applyForce(200f * dt, 0f);
    }
    
    applyForce(0, 300f * dt); // gravity
    applyDrag((float) Math.pow(0.99f, dt / 0.16f));

    body.state.x += body.state.x_vel * dt;
    body.state.y += body.state.y_vel * dt;

    try {
      for (Sprite sprite : sprites) {
        if (sprite == this) {
          continue;
        }

        if (sprite instanceof Platform platform) {
          if (Collision.isColliding(this, platform)) {
            // bounce off the platform
            if (Math.abs(body.state.y_vel) > 10f)
              Util.playSound("boing.wav");
            body.state.y_vel *= -0.5f; // restitution
            body.state.y = platform.body.upY() - RADIUS;
            return;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    graple.update(dt, keys[4]);
  }

  @Override
  public boolean isColliding(Sprite sprite) {
    if (sprite instanceof Player) {
      Player player = (Player) sprite;
      return Collision.isColliding(this.body, player.body);
    }
    throw new RuntimeException("Unhandled collision type");
  }

  @Override
  public void applyDrag(float multiplier) {
    this.body.state.x_vel *= multiplier;
    this.body.state.y_vel *= multiplier;
  }
}
