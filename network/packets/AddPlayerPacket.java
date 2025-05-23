package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class AddPlayerPacket extends Packet {
  public int playerId;
  public String name;
  public String team;

  // for registerPacket compatibility
  public AddPlayerPacket() {
    this.playerId = -1;
    this.name = "";
    this.team = "";
  }

  public AddPlayerPacket(int playerId, String name, String team) {
    this.playerId = playerId;
    this.name = name;
    this.team = team;
  }

  @Override
  public byte getId() {
    return 0x7;
  }

  @Override
  public void read(DataInput in) throws IOException {
    this.playerId = in.readInt();
    this.name = in.readUTF();
    this.team = in.readUTF();
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeInt(playerId);
    out.writeUTF(name);
    out.writeUTF(team);
  }
}
