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
package com.github.tm.stindex.spatial.sfc.zorder;

import java.util.Arrays;
import java.util.BitSet;

import com.github.tm.stindex.ByteArrayUtils;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;

/**
 * Convenience methods used to decode/encode Z-Order space filling curve values (using a simple
 * bit-interleaving approach).
 *
 * @author Yu Liebing
 */
public class ZOrderUtils {
  public static NumericRange[] decodeRanges(
      final byte[] bytes,
      final int bitsPerDimension,
      final SFCDimensionDefinition[] dimensionDefinitions) {
    final byte[] littleEndianBytes = ByteArrayUtils.swapEndianFormat(bytes);
    final BitSet bitSet = BitSet.valueOf(littleEndianBytes);
    final int usedBits = bitsPerDimension * dimensionDefinitions.length;
    final int bitsLength = bytes.length * 8;
    final int bitOffset = bitsLength - usedBits;
    final NumericRange[] normalizedValues = new NumericRange[dimensionDefinitions.length];
    for (int d = 0; d < dimensionDefinitions.length; d++) {
      final BitSet dimensionSet = new BitSet();
      int j = 0;
      for (int i = bitOffset + d; i < bitsLength; i += dimensionDefinitions.length) {
        dimensionSet.set(j++, bitSet.get(i));
      }

      normalizedValues[d] = decode(dimensionSet, bitsPerDimension,0, 1, dimensionDefinitions[d]);
    }

    return normalizedValues;
  }

  public static long[] decodeIndices(
      final byte[] bytes,
      final int bitsPerDimension,
      final int numDimensions) {
    final byte[] littleEndianBytes = ByteArrayUtils.swapEndianFormat(bytes);
    final BitSet bitSet = BitSet.valueOf(littleEndianBytes);
    final long[] coordinates = new long[numDimensions];
    final long rangePerDimension = (long) Math.pow(2, bitsPerDimension);
    for (int d = 0; d < numDimensions; d++) {
      final BitSet dimensionSet = new BitSet();
      int j = 0;
      for (int i = d; i < (bitsPerDimension * numDimensions); i += numDimensions) {
        dimensionSet.set(j++, bitSet.get(i));
      }

      coordinates[d] = decodeIndex(dimensionSet, rangePerDimension);
    }

    return coordinates;
  }

  private static long decodeIndex(final BitSet bs, final long rangePerDimension) {
    long floor = 0;
    long ceiling = rangePerDimension;
    long mid = 0;
    for (int i = 0; i < bs.length(); i++) {
      mid = (floor + ceiling) / 2;
      if (bs.get(i)) {
        floor = mid;
      } else {
        ceiling = mid;
      }
    }
    return mid;
  }

  private static NumericRange decode(
      final BitSet bs,
      final int bitsPerDimension,
      double floor,
      double ceiling,
      final SFCDimensionDefinition dimensionDefinition) {
    double mid = 0;
    for (int i = 0; i < bitsPerDimension; i++) {
      mid = (floor + ceiling) / 2;
      if (bs.get(i)) {
        floor = mid;
      } else {
        ceiling = mid;
      }
    }
    return new NumericRange(
        dimensionDefinition.denormalize(floor),
        dimensionDefinition.denormalize(ceiling));
  }

  /**
   * Interleave the binary representations of each coordinate value.
   * */
  public static byte[] encode(final double[] normalizedValues, final int bitsPerDimension, final int numDimensions) {
    final BitSet[] bitSets = new BitSet[numDimensions];

    for (int d = 0; d < numDimensions; d++) {
      bitSets[d] = getBits(normalizedValues[d], 0, 1, bitsPerDimension);
    }
    final int usedBits = bitsPerDimension * numDimensions;
    final int usedBytes = (int) Math.ceil(usedBits / 8.0);
    final int bitsetLength = usedBytes * 8;
    final int bitOffset = bitsetLength - usedBits;
    // round up to a bitset divisible by 8
    final BitSet combinedBitSet = new BitSet(bitsetLength);
    int j = bitOffset;
    for (int i = 0; i < bitsPerDimension; i++) {
      for (int d = 0; d < numDimensions; d++) {
        combinedBitSet.set(j++, bitSets[d].get(i));
      }
    }
    final byte[] littleEndianBytes = combinedBitSet.toByteArray();
    final byte[] retVal = ByteArrayUtils.swapEndianFormat(littleEndianBytes);
    // ensure bytes length
    if (retVal.length < usedBytes) {
      return Arrays.copyOf(retVal, usedBytes);
    }
    return retVal;
  }

  private static BitSet getBits(final double value, double floor, double ceiling, final int bitsPerDimension) {
    final BitSet buffer = new BitSet(bitsPerDimension);
    for (int i = 0; i < bitsPerDimension; i++) {
      final double mid = (floor + ceiling) / 2;
      if (value >= mid) {
        buffer.set(i);
        floor = mid;
      } else {
        ceiling = mid;
      }
    }
    return buffer;
  }
}
