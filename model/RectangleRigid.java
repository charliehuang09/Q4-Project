package model;

public class RectangleRigid {
  State state;
  float width;
  float height;

  public RectangleRigid(float x, float y) {
    this.state = new State(x, y, 0, 0, 1);
  }

  public float rightX() {
    return this.state.x + width / 2;
  }

  public float leftX() {
    return this.state.x - width / 2;
  }

  public float upY() {
    return this.state.y - height / 2;
  }

  public float downY() {
    return this.state.y + height / 2;
  }
}
