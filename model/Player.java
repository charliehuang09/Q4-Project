package model;

import java.awt.*;

import game.Game;
import struct.MyArrayList;
import util.Util;

public class Player extends Sprite {
  static final float RADIUS = 15;

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

  public void setPosition(float x, float y) {
    this.body.state.x = x;
    this.body.state.y = y;
  }

  @Override
  public boolean canMove() {
    return true;
  }

  @Override
  public void draw(Graphics g) {
    if (!alive)
      return;
    graple.draw(g);

    g.setColor(new Color(0, 0, 0, 100)); // Semi-transparent background
    g.fillOval(
        (int) (body.state.x - RADIUS + 1),
        (int) (body.state.y - RADIUS + 1),
        (int) (RADIUS * 2 + 1),
        (int) (RADIUS * 2 + 1));

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
    if (!alive)
      return;
    body.state.x_vel += x;
    body.state.y_vel += y;
  }

  @Override
  public void applyForce(float x, float y) {
    if (!alive)
      return;
    applyForce(x, y, body.state.mass);
  }

  @Override
  public void update(MyArrayList<Sprite> sprites, float dt, boolean keys[]) {
    if (!alive) {
      return;
    }

    if (keys[0]) { // Up
      applyForce(0, -Game.MOVE_FORCE * dt);
    }
    if (keys[1]) { // Left
      applyForce(-Game.MOVE_FORCE * dt, 0f);
    }
    if (keys[2]) { // Down
      applyForce(0, Game.MOVE_FORCE * dt);
    }
    if (keys[3]) { // Right
      applyForce(Game.MOVE_FORCE * dt, 0f);
    }

    applyForce(0, Game.GRAVITY * dt); // gravity
    applyDrag((float) Math.pow(Game.DRAG_MULTIPLIER, dt / Game.DT_STEP));

    // Update both x and y before checking collisions
    body.state.x += body.state.x_vel * dt;
    body.state.y += body.state.y_vel * dt;

    boolean onGround = false; // Flag to track if the ball is on a platform

    for (Sprite sprite : sprites) {
      if (sprite == this) {
        continue;
      }

      if (sprite instanceof Platform platform) {
        if (Collision.isColliding(this, platform)) {
          if (body.state.y_vel > 0 && body.state.y - RADIUS < platform.body.upY()) {
            // Colliding from above
            body.state.y = platform.body.upY() - RADIUS; // Reposition
            if (Math.abs(body.state.y_vel) > 10f) {
              Util.playSound("boing.wav");
            }
            body.state.y_vel *= -Game.RESTITUTION; // Bounce
            // If you want it to land, consider:
            // if (Math.abs(body.state.y_vel) < THRESHOLD) body.state.y_vel = 0;
            onGround = true; // Mark as on ground
          } else if (body.state.y_vel < 0 && body.state.y + RADIUS > platform.body.downY()) {
            // Colliding from below
            body.state.y = platform.body.downY() + RADIUS; // Reposition
            if (Math.abs(body.state.y_vel) > 10f) {
              Util.playSound("boing.wav");
            }
            body.state.y_vel *= -Game.RESTITUTION; // Bounce
          }

          if (body.state.x_vel > 0 && body.state.x - RADIUS < platform.body.leftX()) {
            // Colliding from left
            body.state.x = platform.body.leftX() - RADIUS; // Reposition
            if (Math.abs(body.state.x_vel) > 10f) {
              Util.playSound("boing.wav");
            }
            body.state.x_vel *= -Game.RESTITUTION; // Bounce
          } else if (body.state.x_vel < 0 && body.state.x + RADIUS > platform.body.rightX()) {
            // Colliding from right
            body.state.x = platform.body.rightX() + RADIUS; // Reposition
            if (Math.abs(body.state.x_vel) > 10f) {
              Util.playSound("boing.wav");
            }
            body.state.x_vel *= -Game.RESTITUTION; // Bounce
          }
        }
      }
    }

    if (onGround) {
      if (keys[0]) { // Up
        applyForce(0, -Game.JUMP_FORCE * dt);
      }

      // Apply some friction to horizontal velocity
      // This is a simplified example; often static/kinetic friction is used
      body.state.x_vel *= Game.GROUND_FRICTION_MULTIPLIER; // e.g., 0.9 for some friction
      if (Math.abs(body.state.x_vel) < 0.1f) { // Stop if very slow
        body.state.x_vel = 0;
      }
    }

    if (Collision.isColliding(this, graple.deathBall)) {
      alive = false;
    }

    if (body.state.y > Game.HEIGHT + RADIUS) {
      alive = false;
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
