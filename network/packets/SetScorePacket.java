package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class SetScorePacket extends Packet {
  public int blueScore;
  public int redScore;

  public SetScorePacket() {
    // Default constructor for serialization
  }

  public SetScorePacket(int blueScore, int redScore) {
    this.blueScore = blueScore;
    this.redScore = redScore;
  }

  @Override
  public byte getId() {
    return 0xc;
  }

  @Override
  public void read(DataInput in) throws IOException {
    blueScore = in.readInt();
    redScore = in.readInt();
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeInt(blueScore);
    out.writeInt(redScore);
  }
}
