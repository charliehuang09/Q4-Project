package network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class Packet {
  protected static int lastTimestamp = 0;
  protected int timestamp;

  public Packet() {
    this.timestamp = lastTimestamp++;
  }

  public abstract byte getId();

  public int getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(int timestamp) {
    this.timestamp = timestamp;
  }

  public void writeHeader(DataOutput out) throws IOException {
    out.writeByte(getId());
    out.writeInt(timestamp);
  }

  public void writeBody(DataOutput out) throws IOException {}

  public void write(DataOutput out) throws IOException {
    writeHeader(out);
    writeBody(out);
  }

  public abstract void read(DataInput in) throws IOException;
}
