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
package com.github.tm.stindex.spatial.sfc;

import java.nio.ByteBuffer;

import com.github.tm.stindex.VarintUtils;
import com.github.tm.stindex.dimension.NumericDimensionDefinition;
import com.github.tm.stindex.dimension.bin.BinRange;
import com.github.tm.stindex.persist.PersistenceUtils;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;

/**
 * This class wraps a dimension definition with a cardinality (bits of precision) on a space filling
 * curve
 */
public class SFCDimensionDefinition implements NumericDimensionDefinition {
  private int bitsOfPrecision;
  private NumericDimensionDefinition dimensionDefinition;

  public SFCDimensionDefinition() {}

  /**
   * @param dimensionDefinition an object which defines a dimension used to create a space filling
   *        curve
   * @param bitsOfPrecision the number of bits associated with the specified dimension object
   */
  public SFCDimensionDefinition(
      final NumericDimensionDefinition dimensionDefinition,
      final int bitsOfPrecision) {
    this.bitsOfPrecision = bitsOfPrecision;
    this.dimensionDefinition = dimensionDefinition;
  }

  @Override
  public NumericData getFullRange() {
    return dimensionDefinition.getFullRange();
  }

  /** @return bitsOfPrecision the bits of precision for the dimension object */
  public int getBitsOfPrecision() {
    return bitsOfPrecision;
  }

  /**
   * @param range numeric data to be normalized
   * @return a BinRange[] based on numeric data
   */
  @Override
  public BinRange[] getNormalizedRanges(final NumericData range) {
    return dimensionDefinition.getNormalizedRanges(range);
  }

  public NumericDimensionDefinition getDimensionDefinition() {
    return dimensionDefinition;
  }

  @Override
  public double normalize(final double value) {
    return dimensionDefinition.normalize(value);
  }

  @Override
  public double denormalize(final double value) {
    return dimensionDefinition.denormalize(value);
  }

  @Override
  public NumericRange getDenormalizedRange(final BinRange range) {
    return dimensionDefinition.getDenormalizedRange(range);
  }

  @Override
  public int getFixedBinIdSize() {
    return dimensionDefinition.getFixedBinIdSize();
  }

  @Override
  public double getRange() {
    return dimensionDefinition.getRange();
  }

  @Override
  public NumericRange getBounds() {
    return dimensionDefinition.getBounds();
  }

  @Override
  public byte[] toBinary() {
    final byte[] dimensionBinary = PersistenceUtils.toBinary(dimensionDefinition);
    final ByteBuffer buf =
        ByteBuffer.allocate(
            dimensionBinary.length + VarintUtils.unsignedIntByteLength(bitsOfPrecision));
    VarintUtils.writeUnsignedInt(bitsOfPrecision, buf);
    buf.put(dimensionBinary);
    return buf.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    bitsOfPrecision = VarintUtils.readUnsignedInt(buf);
    final byte[] dimensionBinary = new byte[buf.remaining()];
    buf.get(dimensionBinary);
    dimensionDefinition = (NumericDimensionDefinition) PersistenceUtils.fromBinary(dimensionBinary);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + bitsOfPrecision;
    result =
        (prime * result) + ((dimensionDefinition == null) ? 0 : dimensionDefinition.hashCode());
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
    final SFCDimensionDefinition other = (SFCDimensionDefinition) obj;
    if (bitsOfPrecision != other.bitsOfPrecision) {
      return false;
    }
    if (dimensionDefinition == null) {
      if (other.dimensionDefinition != null) {
        return false;
      }
    } else if (!dimensionDefinition.equals(other.dimensionDefinition)) {
      return false;
    }
    return true;
  }
}
