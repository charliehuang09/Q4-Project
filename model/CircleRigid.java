package model;

class CircleRigid {
  public float x;
  public float y;
  public float radius;

  public CircleRigid(float x, float y, float radius) {
    this.x = x;
    this.y = y;
  }

  public boolean isColliding(CircleRigid circle) {
    return ((this.x - circle.x) * (this.x - circle.x) + (this.y - circle.y) * (this.y - circle.y)
        < (this.radius + circle.radius) * (this.radius * circle.radius));
  }

  public boolean isColliding(RectangleRigid rectangle) {
    // https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
    float x_dist = Math.abs(this.x - rectangle.x);
    float y_dist = Math.abs(this.y - rectangle.y);

    if (x_dist > (rectangle.width / 2 + this.radius)) {
      return false;
    }
    if (y_dist > (rectangle.height / 2 + this.radius)) {
      return false;
    }

    if (x_dist <= (rectangle.width / 2)) {
      return true;
    }
    if (y_dist <= (rectangle.height / 2)) {
      return true;
    }
    float cornerDistance_sq =
        (x_dist - rectangle.width / 2) * (x_dist - rectangle.width / 2)
            + (y_dist - rectangle.height / 2) * (y_dist - rectangle.height / 2);
    return (cornerDistance_sq <= (this.radius * this.radius));
  }
}
