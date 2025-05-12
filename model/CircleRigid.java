package model;

public class CircleRigid {
  public State state;
  public float radius;

  public CircleRigid(float x, float y, float radius) {
    this.state = new State(x, y, 0, 0, 1);
  }

  public float rightX() {
    return this.state.x + radius;
  }

  public float leftX() {
    return this.state.x - radius;
  }

  public float upY() {
    return this.state.y - radius;
  }

  public float downY() {
    return this.state.y + radius;
  }
}
