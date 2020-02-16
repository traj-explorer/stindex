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
import com.github.stdi_sys.stindex.persist.Persistable;

public class MultiDimensionalCoordinateRangesArray implements Persistable {
  private MultiDimensionalCoordinateRanges[] rangesArray;

  public MultiDimensionalCoordinateRangesArray() {}

  public MultiDimensionalCoordinateRangesArray(
      final MultiDimensionalCoordinateRanges[] rangesArray) {
    this.rangesArray = rangesArray;
  }

  public MultiDimensionalCoordinateRanges[] getRangesArray() {
    return rangesArray;
  }

  @Override
  public byte[] toBinary() {
    final byte[][] rangesBinaries = new byte[rangesArray.length][];
    int binaryLength = VarintUtils.unsignedIntByteLength(rangesBinaries.length);
    for (int i = 0; i < rangesArray.length; i++) {
      rangesBinaries[i] = rangesArray[i].toBinary();
      binaryLength +=
          (VarintUtils.unsignedIntByteLength(rangesBinaries[i].length) + rangesBinaries[i].length);
    }
    final ByteBuffer buf = ByteBuffer.allocate(binaryLength);
    VarintUtils.writeUnsignedInt(rangesBinaries.length, buf);
    for (final byte[] rangesBinary : rangesBinaries) {
      VarintUtils.writeUnsignedInt(rangesBinary.length, buf);
      buf.put(rangesBinary);
    }
    return buf.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    rangesArray = new MultiDimensionalCoordinateRanges[VarintUtils.readUnsignedInt(buf)];
    for (int i = 0; i < rangesArray.length; i++) {
      final byte[] rangesBinary = ByteArrayUtils.safeRead(buf, VarintUtils.readUnsignedInt(buf));
      rangesArray[i] = new MultiDimensionalCoordinateRanges();
      rangesArray[i].fromBinary(rangesBinary);
    }
  }

  public static class ArrayOfArrays implements Persistable {
    private MultiDimensionalCoordinateRangesArray[] coordinateArrays;

    public ArrayOfArrays() {}

    public ArrayOfArrays(final MultiDimensionalCoordinateRangesArray[] coordinateArrays) {
      this.coordinateArrays = coordinateArrays;
    }

    public MultiDimensionalCoordinateRangesArray[] getCoordinateArrays() {
      return coordinateArrays;
    }

    @Override
    public byte[] toBinary() {
      final byte[][] rangesBinaries = new byte[coordinateArrays.length][];
      int binaryLength = VarintUtils.unsignedIntByteLength(rangesBinaries.length);
      for (int i = 0; i < coordinateArrays.length; i++) {
        rangesBinaries[i] = coordinateArrays[i].toBinary();
        binaryLength +=
            (VarintUtils.unsignedIntByteLength(rangesBinaries[i].length)
                + rangesBinaries[i].length);
      }
      final ByteBuffer buf = ByteBuffer.allocate(binaryLength);
      VarintUtils.writeUnsignedInt(rangesBinaries.length, buf);
      for (final byte[] rangesBinary : rangesBinaries) {
        VarintUtils.writeUnsignedInt(rangesBinary.length, buf);
        buf.put(rangesBinary);
      }
      return buf.array();
    }

    @Override
    public void fromBinary(final byte[] bytes) {
      final ByteBuffer buf = ByteBuffer.wrap(bytes);
      final int coordinateArrayLength = VarintUtils.readUnsignedInt(buf);
      ByteArrayUtils.verifyBufferSize(buf, coordinateArrayLength);
      coordinateArrays = new MultiDimensionalCoordinateRangesArray[coordinateArrayLength];
      for (int i = 0; i < coordinateArrayLength; i++) {
        final byte[] rangesBinary = ByteArrayUtils.safeRead(buf, VarintUtils.readUnsignedInt(buf));
        coordinateArrays[i] = new MultiDimensionalCoordinateRangesArray();
        coordinateArrays[i].fromBinary(rangesBinary);
      }
    }
  }
}
