package model;

import java.awt.*;
import struct.MyArrayList;

public class DeathBall extends Sprite {
  static final float RADIUS = 30;
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
    boolean x_collides = false;
    boolean y_collides = false;

    for (Sprite sprite : sprites) {
      if (sprite == this) {
        continue;
      }
      if (sprite instanceof Platform) {
        if (Collision.isColliding(
            (Sprite) this.cloneWithOffset(body.state.x_vel * dt, 0), sprite)) {
          x_collides = true;
        }
        if (Collision.isColliding(
            (Sprite) this.cloneWithOffset(0, body.state.y_vel * dt), sprite)) {
          y_collides = true;
        }
        if (x_collides || y_collides) break;
      }
    }
    if (x_collides) body.state.x_vel *= -1;
    if (y_collides) body.state.y_vel *= -1;
    body.state.x += body.state.x_vel * dt;
    body.state.y += body.state.y_vel * dt;
  }

  @Override
  public void applyDrag(float multiplier) {
    this.body.state.x_vel *= multiplier;
    this.body.state.y_vel *= multiplier;
  }
}
