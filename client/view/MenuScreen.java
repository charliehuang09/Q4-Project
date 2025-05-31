package client.view;

import javax.swing.*;

import java.awt.*;

public class MenuScreen extends JPanel {
  public MenuScreen(ClientScreen clientScreen) {
    this.setLayout(null);

    JLabel titleLabel = new JLabel("Bonk", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
    titleLabel.setBounds(0, 20, 1000, 50);
    this.add(titleLabel);

    JButton joinButton = new JButton("Join");
    joinButton.addActionListener(e -> clientScreen.getController().attemptConnect());
    joinButton.setBounds(400, 200, 200, 50);
    this.add(joinButton);
  }
}
