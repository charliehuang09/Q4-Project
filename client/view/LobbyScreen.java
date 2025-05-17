package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import network.packets.TeamSelectionPacket;
import network.packets.ReadyUpPacket;

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

    JButton selectTeamButton = new JButton("Select Team");
    selectTeamButton.addActionListener(this::selectTeam);
    centerPanel.add(selectTeamButton);

    this.add(centerPanel, BorderLayout.CENTER);

    // Ready Button
    readyButton = new JButton("Ready");
    readyButton.addActionListener(this::toggleReady);
    this.add(readyButton, BorderLayout.SOUTH);

    isReady = false;
  }

  private void selectTeam(ActionEvent e) {
    String[] teams = { "Blue", "Red" };
    String selectedTeam = (String) JOptionPane.showInputDialog(
        this,
        "Select a team:",
        "Team Selection",
        JOptionPane.PLAIN_MESSAGE,
        null,
        teams,
        teams[0]);

    if (selectedTeam != null) {
      teamLabel.setText("Team: " + selectedTeam);
      clientScreen.getPacketManager().sendPacket(new TeamSelectionPacket(selectedTeam));
    }
  }

  private void toggleReady(ActionEvent e) {
    isReady = !isReady;
    readyButton.setText(isReady ? "Unready" : "Ready");
      clientScreen.getPacketManager().sendPacket(new ReadyUpPacket(isReady));
  }
}
