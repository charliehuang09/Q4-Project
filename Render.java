import java.awt.Graphics;
import java.util.ArrayList;
import model.Sprite;

public class Render {
  static void draw(Graphics g, ArrayList<Sprite> sprites) {
    for (Sprite sprite : sprites) {
      sprite.draw(g);
    }
  }
}
