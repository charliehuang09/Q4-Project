package model;

import java.awt.*;

import game.Game;
import struct.MyArrayList;
import util.Util;

public class DeathBall extends Sprite {
  static final float RADIUS = 15;

  public CircleRigid body;

  public DeathBall(float x, float y) {
    this.body = new CircleRigid(x, y, RADIUS);
  }

  public DeathBall cloneWithOffset(float x_offset, float y_offset) {
    DeathBall newDeathBall = new DeathBall(body.state.x, body.state.y);
    newDeathBall.body.state.x += x_offset;
    newDeathBall.body.state.y += y_offset;
    return newDeathBall;
  }

  @Override
  public boolean canMove() {
    return true;
  }

  @Override
  public void draw(Graphics g) {
    g.setColor(new Color(0, 0, 0, 100)); // Semi-transparent background
    g.fillOval(
        (int) (body.state.x - RADIUS + 1),
        (int) (body.state.y - RADIUS + 1),
        (int) (RADIUS * 2 + 1),
        (int) (RADIUS * 2 + 1));

    g.setColor(Color.RED);
    g.fillOval(
        (int) (body.state.x - RADIUS),
        (int) (body.state.y - RADIUS),
        (int) (RADIUS * 2),
        (int) (RADIUS * 2));
    g.setColor(Color.WHITE);
    g.fillOval(
        (int) (body.state.x - RADIUS + 3),
        (int) (body.state.y - RADIUS + 3),
        (int) (RADIUS * 2 - 6),
        (int) (RADIUS * 2 - 6));
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
  public boolean isColliding(Sprite sprite) {
    if (sprite instanceof Player) {
      Player player = (Player) sprite;
      return Collision.isColliding(this.body, player.body);
    }
    throw new RuntimeException("Unhandled collision type");
  }

  @Override
  public void update(MyArrayList<Sprite> sprites, float dt, boolean keys[]) {
    applyForce(0, Game.GRAVITY * dt); // gravity
    applyDrag((float) Math.pow(Game.DRAG_MULTIPLIER, dt / 0.16f));

    // Update both x and y before checking collisions
    body.state.x += body.state.x_vel * dt;
    body.state.y += body.state.y_vel * dt;

    boolean onGround = false; // Flag to track if the ball is on a platform

    for (Sprite sprite : sprites) {
      if (sprite == this) {
        continue;
      }

      if (sprite instanceof Platform platform) {
        if (((Platform) sprite).deathBallCollideable) {
          if (Collision.isColliding(this, platform)) {
            if (body.state.y_vel > 0 && body.state.y - RADIUS < platform.body.upY()) {
              // Colliding from above
              body.state.y = platform.body.upY() - RADIUS; // Reposition
              if (Math.abs(body.state.y_vel) > 10f) {
                Util.playSound("boing.wav");
              }
              body.state.y_vel *= -Game.RESTITUTION; // Bounce
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
    }

    if (onGround) {
      // Apply some friction to horizontal velocity
      body.state.x_vel *= Game.GROUND_FRICTION_MULTIPLIER; // e.g., 0.99 for some friction
      if (Math.abs(body.state.x_vel) < 0.1f) { // Stop if very slow
        body.state.x_vel = 0;
      }
    }
  }

  @Override
  public void applyDrag(float multiplier) {
    this.body.state.x_vel *= multiplier;
    this.body.state.y_vel *= multiplier;
  }

  public boolean blueDeath() {
    return body.state.x < -10;
  }

  public boolean redDeath() {
    return body.state.x > 1010;
  }
}
