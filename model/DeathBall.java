package model;

import java.awt.*;
import struct.MyArrayList;
import util.Util;

public class DeathBall extends Sprite {
  static final float RADIUS = 15;

  private static final float GRAVITY = 150f; // gravity force
  private static final float DRAG_MULTIPLIER = 0.99f; // drag multiplier
  private static final float RESTITUTION = 0.8f; // bounce restitution

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
    g.setColor(Color.GREEN);
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
  public boolean isColliding(Sprite sprite) {
    if (sprite instanceof Player) {
      Player player = (Player) sprite;
      return Collision.isColliding(this.body, player.body);
    }
    throw new RuntimeException("Unhandled collision type");
  }

  @Override
  public void update(MyArrayList<Sprite> sprites, float dt, boolean keys[]) {
    applyForce(0, GRAVITY * dt); // gravity
    applyDrag((float) Math.pow(DRAG_MULTIPLIER, dt / 0.16f));
    
    body.state.y += body.state.y_vel * dt;
    
    for (Sprite sprite : sprites) {
      if (sprite == this) {
        continue;
      }
      
      if (sprite instanceof Platform platform) {
        if (Collision.isColliding(this, platform)) {
          // bounce off the platform
          if (Math.abs(body.state.y_vel) > 10f)
            Util.playSound("boing.wav");
          if (body.state.y_vel > 0)
            body.state.y = platform.body.upY() - RADIUS;
          else
            body.state.y = platform.body.downY() + RADIUS;

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
          if (Math.abs(body.state.x_vel) > 10f)
            Util.playSound("boing.wav");
          
          if (body.state.x_vel > 0)
            body.state.x = platform.body.leftX() - RADIUS;
          else
            body.state.x = platform.body.rightX() + RADIUS;

          body.state.x_vel *= -RESTITUTION; // restitution
          return;
        }
      }
    }
  }

  @Override
  public void applyDrag(float multiplier) {
    this.body.state.x_vel *= multiplier;
    this.body.state.y_vel *= multiplier;
  }
}
