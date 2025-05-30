package network.packets;

import java.io.DataInput;
import java.io.IOException;

import network.Packet;

public class ResetPacket extends Packet {
  public ResetPacket() {}

  @Override
  public byte getId() {
    return 0xb;
  }

  @Override
  public void read(DataInput in) throws IOException {}
}
