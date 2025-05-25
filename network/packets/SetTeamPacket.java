package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class SetTeamPacket extends Packet {
  public int playerId;
  public String team;

  public SetTeamPacket() {
  }

  public SetTeamPacket(int playerId, String team) {
    this.playerId = playerId;
    this.team = team;
  }

  @Override
  public byte getId() {
    return 0x8; // Unique ID for this packet
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeInt(playerId);
    out.writeUTF(team);
  }

  @Override
  public void read(DataInput in) throws IOException {
    this.playerId = in.readInt();
    this.team = in.readUTF();
  }
}
