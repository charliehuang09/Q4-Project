package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class TeamSelectionPacket extends Packet {
  public String team;

  public TeamSelectionPacket() {
  }

  public TeamSelectionPacket(String team) {
    this.team = team;
  }

  @Override
  public byte getId() {
    return 0x3; // Unique ID for this packet
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeUTF(team);
  }

  @Override
  public void read(DataInput in) throws IOException {
    this.team = in.readUTF();
  }
}
