package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LobbyScreen extends JPanel {
  private ClientScreen clientScreen;
  private JLabel teamLabel;
  private JButton readyButton;
  private boolean isReady;

  public LobbyScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;
    this.setLayout(new BorderLayout());

    JLabel titleLabel = new JLabel("Lobby", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    this.add(titleLabel, BorderLayout.NORTH);

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new GridLayout(2, 1));

    // Team Selection
    teamLabel = new JLabel("Team: None", SwingConstants.CENTER);
    centerPanel.add(teamLabel);

    JButton blueTeamButton = new JButton("Join Blue Team");
    blueTeamButton.addActionListener(_ -> joinTeam("Blue"));
    centerPanel.add(blueTeamButton);

    JButton redTeamButton = new JButton("Join Red Team");
    redTeamButton.addActionListener(_ -> joinTeam("Red"));
    centerPanel.add(redTeamButton);

    this.add(centerPanel, BorderLayout.CENTER);

    // Ready Button
    readyButton = new JButton("Ready");
    readyButton.addActionListener(this::toggleReady);
    this.add(readyButton, BorderLayout.SOUTH);

    isReady = false;
  }

  private void joinTeam(String team) {
    teamLabel.setText("Team: " + team);
    clientScreen.getController().joinTeam(team);
  }

  private void toggleReady(ActionEvent e) {
    isReady = !isReady;
    readyButton.setText(isReady ? "Unready" : "Ready");
    clientScreen.getController().readyUp(isReady);
  }
}
