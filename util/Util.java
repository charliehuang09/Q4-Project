package util;

import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Util {
  public static void playSound(String filename) {
    try {
      InputStream inputStream = Util.class.getResourceAsStream("../sound/" + filename);
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(inputStream));
      clip.start();
    } catch (Exception e) {
      System.out.println("Error playing sound: " + e.getMessage());
    }
  }
}
