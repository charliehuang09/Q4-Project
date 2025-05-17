package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class MenuScreen extends JPanel {
  private ClientScreen clientScreen;

  public MenuScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;
    this.setLayout(new BorderLayout());

    JLabel titleLabel = new JLabel("Game Menu", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    this.add(titleLabel, BorderLayout.NORTH);

    JButton joinButton = new JButton("Join");
    joinButton.addActionListener(this::attemptConnect);
    this.add(joinButton, BorderLayout.CENTER);
  }

  public void attemptConnect(ActionEvent e) {
    try {
      clientScreen.getPacketManager().connect("localhost", 31415);
      clientScreen.setCurrentState(ClientScreen.GameState.LOBBY);
    } catch (IOException ex) {
      System.out.println("Can't connect to server: " + ex.getMessage());
    }
  }
}
