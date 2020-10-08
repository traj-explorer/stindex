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
package com.github.tm.stindex.dimension;

import com.github.tm.stindex.dimension.bin.BinRange;
import com.github.tm.stindex.dimension.bin.BinningStrategy;
import com.github.tm.stindex.persist.PersistenceUtils;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;

/**
 * Because space filling curves require an extent (minimum & maximum), the unbounded implementation
 * relies on an external binning strategy to translate an unbounded variable into bounded bins
 */
public class UnboundedDimensionDefinition extends BasicDimensionDefinition {

  protected BinningStrategy binningStrategy;

  public UnboundedDimensionDefinition() {
    super();
  }

  /** @param binningStrategy a bin strategy associated with the dimension */
  public UnboundedDimensionDefinition(final BinningStrategy binningStrategy) {
    super(binningStrategy.getBinMin(), binningStrategy.getBinMax());
    this.binningStrategy = binningStrategy;
  }

  /** @param index a numeric value to be normalized */
  @Override
  public BinRange[] getNormalizedRanges(final NumericData index) {
    return binningStrategy.getNormalizedRanges(index);
  }

  /** @return a bin strategy associated with the dimension */
  public BinningStrategy getBinningStrategy() {
    return binningStrategy;
  }

  @Override
  public NumericRange getDenormalizedRange(final BinRange range) {
    return binningStrategy.getDenormalizedRanges(range);
  }

  @Override
  public int getFixedBinIdSize() {
    return binningStrategy.getFixedBinIdSize();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((binningStrategy == null) ? 0 : binningStrategy.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final UnboundedDimensionDefinition other = (UnboundedDimensionDefinition) obj;
    if (binningStrategy == null) {
      if (other.binningStrategy != null) {
        return false;
      }
    } else if (!binningStrategy.equals(other.binningStrategy)) {
      return false;
    }
    return true;
  }

  @Override
  public byte[] toBinary() {
    return PersistenceUtils.toBinary(binningStrategy);
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    binningStrategy = (BinningStrategy) PersistenceUtils.fromBinary(bytes);
    min = binningStrategy.getBinMin();
    max = binningStrategy.getBinMax();
  }
}
