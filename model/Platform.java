package model;

import java.awt.*;
import struct.MyArrayList;

public class Platform extends Sprite {
  public RectangleRigid body;

  public Platform(float x, float y, float width, float height) {
    this.body = new RectangleRigid(x, y, width, height);
  }

  @Override
  public boolean canMove() {
    return false;
  }

  @Override
  public void draw(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(
        (int) (body.state.x - (body.width / 2)),
        (int) (body.state.y - (body.height / 2)),
        (int) body.width,
        (int) body.height);
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
  public void update(MyArrayList<Sprite> sprites, float dt) {}

  @Override
  public boolean isColliding(Sprite sprite) {
    return true;
    // if (sprite instanceof Player) {
    //   Player player = (Player) sprite;
    //   return Collision.isColliding(this.body, player.body);
    // }
    // throw new RuntimeException("Unhandled collision type");
  }

  @Override
  public void applyDrag(float multiplier) {
    this.body.state.x_vel *= multiplier;
    this.body.state.y_vel *= multiplier;
  }
}
