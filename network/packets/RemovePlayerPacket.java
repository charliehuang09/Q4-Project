package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class RemovePlayerPacket extends Packet {
  public int playerId;

  // for registerPacket compatibility
  public RemovePlayerPacket() {
    this.playerId = -1;
  }

  public RemovePlayerPacket(int playerId) {
    this.playerId = playerId;
  }

  @Override
  public byte getId() {
    return 0x9;
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
