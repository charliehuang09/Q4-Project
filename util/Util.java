package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Util {
  public static synchronized void playSound(final String fileName) {
    new Thread(new Runnable() {
      public void run() {
        try {
          File soundFile = new File("./sound/" + fileName);
          AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

          Clip clip = AudioSystem.getClip();
          clip.open(audioIn);
          clip.start();

          clip.addLineListener(event -> {
            if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
              clip.close();
            }
          });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static BufferedImage load(String filename) {
    try {
      return ImageIO.read(new File(filename));
    } catch (IOException e) {
      System.out.println(filename);
      e.printStackTrace();
      return null;
    }
  }
}