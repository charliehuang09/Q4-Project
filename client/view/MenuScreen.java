package client.view;

import java.awt.*;
import javax.swing.*;

public class MenuScreen extends JPanel {
  public MenuScreen(ClientScreen clientScreen) {
    this.setLayout(null);

    JLabel titleLabel = new JLabel("Bonk", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Bahnschrift", Font.BOLD, 50));
    titleLabel.setBounds(0, 20, 1000, 50);
    this.add(titleLabel);

    JButton joinButton = new JButton("Join");
    joinButton.addActionListener(e -> clientScreen.getController().attemptConnect());
    joinButton.setBounds(400, 100, 200, 50);
    this.add(joinButton);

    String instructions[] = "Instructions:\nWASD to move\nz to graple\ny is cheat key".split("\n");
    int idx = 0;
    for (String instruction : instructions) {
      JLabel instructionLabel = new JLabel(instruction, SwingConstants.CENTER);
      instructionLabel.setFont(new Font("Bahnschrift", Font.BOLD, 50));
      instructionLabel.setBounds(0, 200 + (50 * idx), 1000, 50);
      this.add(instructionLabel);
      idx += 1;
    }
  }
}
