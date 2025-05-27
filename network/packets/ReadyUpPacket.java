package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class ReadyUpPacket extends Packet {
  public boolean isReady;

  public ReadyUpPacket() {}

  public ReadyUpPacket(boolean isReady) {
    this.isReady = isReady;
  }

  @Override
  public byte getId() {
    return 0x4; // Unique ID for this packet
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeBoolean(isReady);
  }

  @Override
  public void read(DataInput in) throws IOException {
    this.isReady = in.readBoolean();
  }
}
