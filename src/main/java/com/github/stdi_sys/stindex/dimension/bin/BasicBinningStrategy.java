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
package com.github.stdi_sys.stindex.dimension.bin;

import java.nio.ByteBuffer;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericData;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericRange;

public class BasicBinningStrategy implements BinningStrategy {
  private double interval;
  private double halfInterval;

  public BasicBinningStrategy() {
    super();
  }

  public BasicBinningStrategy(final double interval) {
    super();
    this.interval = interval;
    halfInterval = interval / 2;
  }

  @Override
  public byte[] toBinary() {
    final ByteBuffer buf = ByteBuffer.allocate(8);
    buf.putDouble(interval);
    return buf.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    interval = buf.getDouble();
    halfInterval = interval / 2;
  }

  @Override
  public double getBinMin() {
    return -halfInterval;
  }

  @Override
  public double getBinMax() {
    return halfInterval;
  }

  @Override
  public BinValue getBinnedValue(final double value) {
    final double bin = Math.floor((value - halfInterval) / interval);
    return new BinValue(intToBinary((int) bin), (value - (interval * bin)));
  }

  private static byte[] intToBinary(final int bin) {
    final ByteBuffer buf = ByteBuffer.allocate(4);
    buf.putInt(bin);
    return buf.array();
  }

  @Override
  public BinRange[] getNormalizedRanges(final NumericData index) {
    if (!index.isRange()) {
      final BinValue value = getBinnedValue(index.getMin());
      return new BinRange[] {
          new BinRange(
              value.getBinId(),
              value.getNormalizedValue(),
              value.getNormalizedValue(),
              false)};
    }
    final int minBin = (int) Math.ceil((index.getMin() - halfInterval) / interval);
    final int maxBin = (int) Math.ceil((index.getMax() - halfInterval) / interval);
    if (minBin == maxBin) {
      final double min = (index.getMin() - (interval * minBin));
      final double max = (index.getMax() - (interval * maxBin));
      final ByteBuffer buf = ByteBuffer.allocate(4);
      buf.putInt(minBin);
      return new BinRange[] {new BinRange(buf.array(), min, max, false)};
    }
    final BinRange[] retVal = new BinRange[(maxBin - minBin) + 1];
    retVal[0] =
        new BinRange(
            intToBinary(minBin),
            (index.getMin() - (interval * minBin)),
            halfInterval,
            false);
    for (int b = minBin + 1; b < maxBin; b++) {
      retVal[b - minBin] = new BinRange(intToBinary(b), -halfInterval, halfInterval, true);
    }
    retVal[maxBin - minBin] =
        new BinRange(
            intToBinary(maxBin),
            -halfInterval,
            (index.getMax() - (interval * maxBin)),
            false);
    return retVal;
  }

  @Override
  public NumericRange getDenormalizedRanges(final BinRange binnedRange) {
    final int bin = ByteBuffer.wrap(binnedRange.getBinId()).getInt();
    final double center = bin * interval;
    if (binnedRange.isFullExtent()) {
      return new NumericRange(center - halfInterval, center + halfInterval);
    }

    return new NumericRange(
        center + binnedRange.getNormalizedMin(),
        center + binnedRange.getNormalizedMax());
  }

  @Override
  public int getFixedBinIdSize() {
    return 4;
  }
}
