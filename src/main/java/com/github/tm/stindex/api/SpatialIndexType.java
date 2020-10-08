package com.github.tm.stindex.api;

/**
 * @author Yu Liebing
 */
public enum SpatialIndexType {
  ZOrder((byte) 0),
  Hilbert((byte) 1),
  XZOrder((byte) 2),
  H3((byte) 3);

  private byte value;

  SpatialIndexType(byte value) {
    this.value = value;
  }

  public byte getValue() {
    return value;
  }

  public static SpatialIndexType valueOf(byte type) {
    switch (type) {
      case 0:
        return ZOrder;
      case 1:
        return Hilbert;
      case 2:
        return XZOrder;
      case 3:
        return H3;
      default:
        return null;
    }
  }
}
