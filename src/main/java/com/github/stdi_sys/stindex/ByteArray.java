/*
 * Copyright 2020 Yu Liebing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */
package com.github.stdi_sys.stindex;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import com.google.common.primitives.Longs;

/**
 * This class is a wrapper around a byte array to ensure equals and hashcode operations use the
 * values of the bytes rather than explicit object identity. And Override {@code toString} function
 * for printing convenience.
 *
 * @author Yu Liebing
 */
public class ByteArray implements Comparable<ByteArray> {
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

  protected byte[] bytes;
  protected String string;

  public ByteArray() {
    this(EMPTY_BYTE_ARRAY);
  }

  public ByteArray(final byte[] bytes) {
    this.bytes = bytes;
  }

  public ByteArray(final String string) {
    bytes = StringUtils.stringToBinary(string);
    this.string = string;
  }

  public ByteArray(final long value) {
    bytes = ByteArrayUtils.longToByteArray(value);
  }

  public ByteArray(final List<Byte> bytes) {
    this.bytes = new byte[bytes.size()];
    for (int i = 0; i < bytes.size(); i++) {
      this.bytes[i] = bytes.get(i);
    }
  }

  public byte[] getBytes() {
    return bytes;
  }

  public int length() {
    return bytes.length;
  }

  public byte[] getNextPrefix() {
    return ByteArrayUtils.getNextPrefix(bytes);
  }

  public String getHexString() {
    return ByteArrayUtils.getHexString(bytes);
  }

  public long toLong() {
    if (bytes.length <= 8) {
      byte[] longBytes = new byte[8];
      System.arraycopy(bytes, 0, longBytes, 8 - bytes.length, bytes.length);
      return Longs.fromByteArray(longBytes);
    }
    // if bytes.length > 8, we only use the last 8 bytes
    byte[] longBytes = new byte[8];
    System.arraycopy(bytes, bytes.length - 8, longBytes, 0, 8);
    return Longs.fromByteArray(longBytes);
  }

  public ByteArray combine(ByteArray other) {
    return new ByteArray(ByteArrayUtils.combineArrays(bytes, other.getBytes()));
  }

  public ByteArray slice(int from, int to) {
    if ((to - from) > bytes.length || from >= bytes.length) {
      throw new IllegalArgumentException("Invalid slice range.");
    }
    byte[] slice = new byte[to-from];
    System.arraycopy(bytes, from, slice, 0, slice.length);
    return new ByteArray(slice);
  }

  @Override
  public String toString() {
    if (string != null) {
      return string;
    }
    string = getHexString();
    return string;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(bytes);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ByteArray other = (ByteArray) obj;
    return Arrays.equals(bytes, other.bytes);
  }

  @Override
  public int compareTo(final ByteArray o) {
    return ByteArrayUtils.compare(bytes, o.bytes);
  }

  public static byte[] toBytes(final ByteArray[] ids) {
    int len = VarintUtils.unsignedIntByteLength(ids.length);
    for (final ByteArray id : ids) {
      len += (id.bytes.length + VarintUtils.unsignedIntByteLength(id.bytes.length));
    }
    final ByteBuffer buffer = ByteBuffer.allocate(len);
    VarintUtils.writeUnsignedInt(ids.length, buffer);
    for (final ByteArray id : ids) {
      VarintUtils.writeUnsignedInt(id.bytes.length, buffer);
      buffer.put(id.bytes);
    }
    return buffer.array();
  }

  public static ByteArray[] fromBytes(final byte[] idData) {
    final ByteBuffer buffer = ByteBuffer.wrap(idData);
    final int len = VarintUtils.readUnsignedInt(buffer);
    ByteArrayUtils.verifyBufferSize(buffer, len);
    final ByteArray[] result = new ByteArray[len];
    for (int i = 0; i < len; i++) {
      final int idSize = VarintUtils.readUnsignedInt(buffer);
      final byte[] id = ByteArrayUtils.safeRead(buffer, idSize);
      result[i] = new ByteArray(id);
    }
    return result;
  }
}
