package model;

import java.awt.*;
import struct.MyArrayList;
import util.Util;

public class Player extends Sprite {
  static final float RADIUS = 15;

  private static final int FPS = 60; // frames per second
  private static final float DT_STEP = 1000f / FPS; // time step for drag calculation
  private static final float RESTITUTION = 0.8f; // bounce off platforms
  private static final float DRAG_MULTIPLIER = 0.995f;
  private static final float GRAVITY = 150f; // gravity force
  private static final float MOVE_FORCE = 200f; // force applied when moving

  public String name;
  public Team team;
  public CircleRigid body;
  public boolean alive;
  public Graple graple;

  public Player(float x, float y, String name, Team team, DeathBall deathBall) {
    this.alive = true;
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
    if (!alive) return;
    graple.draw(g);

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
    if (!alive) return;
    body.state.x_vel += x;
    body.state.y_vel += y;
  }

  @Override
  public void applyForce(float x, float y) {
    if (!alive) return;
    applyForce(x, y, body.state.mass);
  }

  @Override
  public void update(MyArrayList<Sprite> sprites, float dt, boolean keys[]) {
    if (!alive) return;
    if (keys[0]) {
      applyForce(0, -MOVE_FORCE * dt);
    }
    if (keys[1]) {
      applyForce(-MOVE_FORCE * dt, 0f);
    }
    if (keys[2]) {
      applyForce(0, MOVE_FORCE * dt);
    }
    if (keys[3]) {
      applyForce(MOVE_FORCE * dt, 0f);
    }

    applyForce(0, GRAVITY * dt); // gravity
    applyDrag((float) Math.pow(DRAG_MULTIPLIER, dt / DT_STEP));

    body.state.y += body.state.y_vel * dt;

    for (Sprite sprite : sprites) {
      if (sprite == this) {
        continue;
      }

      if (sprite instanceof Platform platform) {
        if (Collision.isColliding(this, platform)) {
          // bounce off the platform
          if (Math.abs(body.state.y_vel) > 10f) Util.playSound("boing.wav");
          if (body.state.y_vel > 0) body.state.y = platform.body.upY() - RADIUS;
          else body.state.y = platform.body.downY() + RADIUS;

          body.state.y_vel *= -RESTITUTION; // restitution
          return;
        }
      }
    }

    body.state.x += body.state.x_vel * dt;

    for (Sprite sprite : sprites) {
      if (sprite == this) {
        continue;
      }

      if (sprite instanceof Platform platform) {
        if (Collision.isColliding(this, platform)) {
          // bounce off the platform
          if (Math.abs(body.state.x_vel) > 10f) Util.playSound("boing.wav");

          if (body.state.x_vel > 0) body.state.x = platform.body.leftX() - RADIUS;
          else body.state.x = platform.body.rightX() + RADIUS;

          body.state.x_vel *= -RESTITUTION; // restitution
          return;
        }
      }
    }

    if (Collision.isColliding((Sprite) this, (Sprite) graple.deathBall)) alive = false;

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
