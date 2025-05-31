package client.view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameOverScreen extends JPanel {
  private JLabel winnerLabel;

  public GameOverScreen(ClientScreen clientScreen) {
    this.setLayout(null);

    JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
    gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
    gameOverLabel.setBounds(0, 20, 1000, 50);
    this.add(gameOverLabel);

    winnerLabel = new JLabel("", SwingConstants.CENTER);
    winnerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
    winnerLabel.setBounds(0, 100, 1000, 30);
    this.add(winnerLabel);
  }

  public void updateScore(int blueScore, int redScore) {
    String winnerName = blueScore >= 3 ? "Blue Team" : "Red Team";
    winnerLabel.setText(winnerName + " won! (" + blueScore + " - " + redScore + ")");
  }
}
