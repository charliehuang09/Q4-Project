package client.view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import client.ClientController;

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

    JButton backToMenuButton = new JButton("Back to Lobby");
    backToMenuButton.setFont(new Font("Arial", Font.PLAIN, 20));
    backToMenuButton.setBounds(400, 200, 200, 50);
    backToMenuButton.addActionListener(e -> {
      clientScreen.getController().setCurrentState(ClientController.GameState.LOBBY);
    });
    this.add(backToMenuButton);
  }

  public void updateScore(int blueScore, int redScore) {
    String winnerName = blueScore > redScore ? "Blue Team" : "Red Team";
    winnerLabel.setText(winnerName + " won! (" + blueScore + " - " + redScore + ")");
  }
}
