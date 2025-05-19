package model;

public class Collision {

  public static boolean isColliding(CircleRigid circle1, CircleRigid circle2) {
    return ((circle1.state.x - circle2.state.x) * (circle1.state.x - circle2.state.x)
            + (circle1.state.y - circle2.state.y) * (circle1.state.y - circle2.state.y)
        < (circle1.radius + circle2.radius) * (circle1.radius * circle2.radius));
  }

  public static boolean isColliding(RectangleRigid rectangle1, RectangleRigid rectangle2) {
    if (rectangle1.rightX() < rectangle2.leftX()) return false;
    if (rectangle2.rightX() < rectangle1.leftX()) return false;

    if (rectangle1.downY() < rectangle2.upY()) return false;
    if (rectangle2.downY() < rectangle1.upY()) return false;

    return true;
  }

  public static boolean isColliding(CircleRigid circle, RectangleRigid rectangle) {
    if (circle.rightX() < rectangle.leftX()) return false;
    if (rectangle.rightX() < circle.leftX()) return false;

    if (circle.downY() < rectangle.upY()) return false;
    if (rectangle.downY() < circle.upY()) return false;

    return true;
  }

  public static boolean isColliding(RectangleRigid rectangle, CircleRigid circle) {
    return isColliding(circle, rectangle);
  }

  public static boolean isColliding(Sprite sprite1, Sprite sprite2) {
    if (sprite1 instanceof Player && sprite2 instanceof Player) {
      Player player1 = (Player) sprite1;
      Player player2 = (Player) sprite2;
      return isColliding(player1.body, player2.body);
    }
    if (sprite1 instanceof Player && sprite2 instanceof Platform) {
      Player player = (Player) sprite1;
      Platform platform = (Platform) sprite2;
      return isColliding(player.body, platform.body);
    }
    if (sprite1 instanceof Platform && sprite2 instanceof Player) {
      Platform platform = (Platform) sprite1;
      Player player = (Player) sprite2;
      return isColliding(platform.body, player.body);
    }
    if (sprite1 instanceof Platform && sprite2 instanceof Platform) {
      Platform platfrom1 = (Platform) sprite1;
      Platform platform2 = (Platform) sprite2;
      return isColliding(platfrom1.body, platform2.body);
    }
    return false;
  }
}
