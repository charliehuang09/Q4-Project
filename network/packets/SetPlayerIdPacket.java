package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class SetPlayerIdPacket extends Packet {
  public int playerId;

  // for registerPacket compatibility
  public SetPlayerIdPacket() {
    this.playerId = -1;
  }

  public SetPlayerIdPacket(int playerId) {
    this.playerId = playerId;
  }

  @Override
  public byte getId() {
    return 0x6;
  }

  @Override
  public void read(DataInput in) throws IOException {
    this.playerId = in.readInt();
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeInt(playerId);
  }
}
