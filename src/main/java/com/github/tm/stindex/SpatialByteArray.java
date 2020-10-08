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
package com.github.tm.stindex;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * @author Yu Liebing
 */
public class SpatialByteArray extends ByteArray {

  private int res;

  public SpatialByteArray() {
    this(EMPTY_BYTE_ARRAY, 0);
  }

  public SpatialByteArray(final byte[] bytes, int res) {
    this.bytes = bytes;
    this.res = res;
  }

  public SpatialByteArray(final String string, int res) {
    bytes = StringUtils.stringToBinary(string);
    this.string = string;
    this.res = res;
  }

  public SpatialByteArray(final long value, int res) {
    bytes = ByteArrayUtils.longToByteArray(value);
    this.res = res;
  }

  public SpatialByteArray(final List<Byte> bytes, int res) {
    this.bytes = new byte[bytes.size()];
    for (int i = 0; i < bytes.size(); i++) {
      this.bytes[i] = bytes.get(i);
    }
    this.res = res;
  }

  @Override
  public String toString() {
    if (string != null) {
      return string;
    }
    // only to long when small then the max long value
    if (bytes.length > 8) {
      string = getHexString();
    } else {
      string = toLong() + "";
    }
    return string;
  }

  @Override
  public int getRes() {
    return res;
  }

  public static ByteArrayRange transform(SpatialByteArray index, int toRes) {
    int curBits = index.res * 2;
    int curBytes = index.length();
    int curOffset = curBytes * 8 - curBits;

    int toBits = toRes * 2;
    int toBytes = (toBits + 7) / 8;
    int toOffset = toBytes * 8 - toBits;

    BitSet originBitSet = BitSet.valueOf(ByteArrayUtils.swapEndianFormat(index.bytes));
    BitSet toBitSet = new BitSet(toBytes * 8);
    for (int i = curOffset; i < curBytes * 8; i++) {
      toBitSet.set(toOffset + i - curOffset, originBitSet.get(i));
    }
    byte[] startBytes = ByteArrayUtils.swapEndianFormat(toBitSet.toByteArray());
    if (startBytes.length < toBytes) {
      startBytes = Arrays.copyOf(startBytes, toBytes);
    }
    ByteArray start = new SpatialByteArray(startBytes, toRes);
    for (int i = toOffset + curBits; i < toBytes * 8; i++) {
      toBitSet.set(i, true);
    }
    byte[] endBytes = ByteArrayUtils.swapEndianFormat(toBitSet.toByteArray());
    if (endBytes.length < toBytes) {
      endBytes = Arrays.copyOf(endBytes, toBytes);
    }
    ByteArray end = new SpatialByteArray(endBytes, toRes);
    return new ByteArrayRange(start, end);
  }
}
