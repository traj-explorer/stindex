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
import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import org.junit.Assert;
import org.junit.Test;

public class BasicDimensionDefinitionTest {

  private final double MINIMUM = 20;
  private final double MAXIMUM = 100;
  private final double DELTA = 1e-15;

  @Test
  public void testNormalizeMidValue() {

    final double midValue = 60;
    final double normalizedValue = 0.5;

    Assert.assertEquals(
        normalizedValue,
        getNormalizedValueUsingBounds(MINIMUM, MAXIMUM, midValue),
        DELTA);
  }

  @Test
  public void testNormalizeUpperValue() {

    final double lowerValue = 20;
    final double normalizedValue = 0.0;

    Assert.assertEquals(
        normalizedValue,
        getNormalizedValueUsingBounds(MINIMUM, MAXIMUM, lowerValue),
        DELTA);
  }

  @Test
  public void testNormalizeLowerValue() {

    final double upperValue = 100;
    final double normalizedValue = 1.0;

    Assert.assertEquals(
        normalizedValue,
        getNormalizedValueUsingBounds(MINIMUM, MAXIMUM, upperValue),
        DELTA);
  }

  @Test
  public void testNormalizeClampOutOfBoundsValue() {

    final double value = 1;
    final double normalizedValue = 0.0;

    Assert.assertEquals(
        normalizedValue,
        getNormalizedValueUsingBounds(MINIMUM, MAXIMUM, value),
        DELTA);
  }

  @Test
  public void testNormalizeRangesBinRangeCount() {

    final double minRange = 40;
    final double maxRange = 50;
    final int binCount = 1;

    final BinRange[] binRange = getNormalizedRangesUsingBounds(minRange, maxRange);

    Assert.assertEquals(binCount, binRange.length);
  }

  @Test
  public void testNormalizeClampOutOfBoundsRanges() {

    final double minRange = 1;
    final double maxRange = 150;

    final BinRange[] binRange = getNormalizedRangesUsingBounds(minRange, maxRange);

    Assert.assertEquals(MINIMUM, binRange[0].getNormalizedMin(), DELTA);
    Assert.assertEquals(MAXIMUM, binRange[0].getNormalizedMax(), DELTA);
  }

  private double getNormalizedValueUsingBounds(
      final double min,
      final double max,
      final double value) {
    return new BasicDimensionDefinition(min, max).normalize(value);
  }

  private BinRange[] getNormalizedRangesUsingBounds(final double minRange, final double maxRange) {

    return new BasicDimensionDefinition(MINIMUM, MAXIMUM).getNormalizedRanges(
        new NumericRange(minRange, maxRange));
  }
}
