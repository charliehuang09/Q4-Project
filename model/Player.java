package model;

import java.awt.*;

public class Player extends Sprite {
  static final float RADIUS = 30;
  private Team team;
  private CircleRigid body;

  public Player(float x, float y, Team team) {
    this.team = team;
    this.body = new CircleRigid(x, y, RADIUS);
  }

  @Override
  public boolean canMove() {
    return true;
  }

  @Override
  public void draw(Graphics g) {
    g.drawOval((int) body.state.y, (int) body.state.x, (int) RADIUS, (int) RADIUS);
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
  public void update() {
    body.state.x += body.state.y_vel;
    body.state.y += body.state.x_vel;
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
