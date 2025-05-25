package model;

import java.awt.*;
import struct.MyArrayList;

public class Player extends Sprite {
  static final float RADIUS = 30;
  static final float MIDDLE = 500;
  static final float MOVE_STRENGTH = 0.5f;
  private Team team;
  public CircleRigid body;
  public Graple graple;

  public Player(float x, float y, Team team, DeathBall deathBall) {
    this.team = team;
    this.body = new CircleRigid(x, y, RADIUS);
    this.graple = new Graple(this, deathBall);
  }

  public Player cloneWithOffset(float x_offset, float y_offset) {
    Player newPlayer = new Player(body.state.x, body.state.y, team, null);
    newPlayer.body.state.x += x_offset;
    newPlayer.body.state.y += y_offset;
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
        (int) (body.state.x - (RADIUS)),
        (int) (body.state.y - (RADIUS)),
        (int) RADIUS,
        (int) RADIUS);
    graple.draw(g);
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
      applyForce(0, MOVE_STRENGTH * -dt);
    }
    if (keys[1]) {
      applyForce(MOVE_STRENGTH * -dt, 0f);
    }
    if (keys[2]) {
      applyForce(0, MOVE_STRENGTH * dt);
    }
    if (keys[3]) {
      applyForce(MOVE_STRENGTH * dt, 0f);
    }

    boolean x_collides = false;
    boolean y_collides = false;
    Platform plt = null;

    if (this.team == Team.RED) {
      if (body.rightX() > MIDDLE) {
        x_collides = true;
      }
    } else {
      if (body.leftX() < MIDDLE) {
        x_collides = true;
      }
    }

    for (Sprite sprite : sprites) {
      if (sprite == this) {
        continue;
      }
      if (sprite instanceof Platform) {
        plt = (Platform) sprite;
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
