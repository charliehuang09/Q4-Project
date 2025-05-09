package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class Packet {
  protected static int lastTimestamp = 0;
  protected int timestamp;

  public Packet() {
    this.timestamp = lastTimestamp++;
  }

  public abstract byte getId();

  public byte[] getHeader() {
    ByteBuffer b = ByteBuffer.allocate(1 + 4);
    b.put(getId());
    b.putInt(timestamp);
    return b.array();
  }

  public byte[] getBody() {
    return new byte[0];
  }

  public abstract void readPacket(BufferedReader in) throws IOException;

  public byte[] asBytes() {
    byte[] header = getHeader();
    byte[] body = getBody();

    byte[] bytes = new byte[header.length + body.length];
    
    int i = 0;
    for (Byte b : header)
      bytes[i++] = b;
    for (Byte b : body)
      bytes[i++] = b;

    return bytes;
  }
}
