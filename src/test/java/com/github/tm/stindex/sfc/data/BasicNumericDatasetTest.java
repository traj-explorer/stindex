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
package com.github.tm.stindex.sfc.data;

import com.github.tm.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import com.github.tm.stindex.spatial.sfc.data.NumericValue;
import org.junit.Assert;
import org.junit.Test;

public class BasicNumericDatasetTest {

  private final double DELTA = 1e-15;

  private final BasicNumericDataset basicNumericDatasetRanges =
      new BasicNumericDataset(
          new NumericData[] {
              new NumericRange(10, 50),
              new NumericRange(25, 95),
              new NumericRange(-50, 50)});

  private final BasicNumericDataset basicNumericDatasetValues =
      new BasicNumericDataset(
          new NumericData[] {new NumericValue(25), new NumericValue(60), new NumericValue(0)});

  @Test
  public void testNumericRangesMinValues() {

    final int expectedCount = 3;
    final double[] expectedMinValues = new double[] {10, 25, -50};
    final double[] mins = basicNumericDatasetRanges.getMinValuesPerDimension();

    Assert.assertEquals(expectedCount, basicNumericDatasetRanges.getDimensionCount());

    for (int i = 0; i < basicNumericDatasetRanges.getDimensionCount(); i++) {
      Assert.assertEquals(expectedMinValues[i], mins[i], DELTA);
    }
  }

  @Test
  public void testNumericRangesMaxValues() {

    final int expectedCount = 3;
    final double[] expectedMaxValues = new double[] {50, 95, 50};
    final double[] max = basicNumericDatasetRanges.getMaxValuesPerDimension();

    Assert.assertEquals(expectedCount, basicNumericDatasetRanges.getDimensionCount());

    for (int i = 0; i < basicNumericDatasetRanges.getDimensionCount(); i++) {
      Assert.assertEquals(expectedMaxValues[i], max[i], DELTA);
    }
  }

  @Test
  public void testNumericRangesCentroidValues() {

    final int expectedCount = 3;
    final double[] expectedCentroidValues = new double[] {30, 60, 0};
    final double[] centroid = basicNumericDatasetRanges.getCentroidPerDimension();

    Assert.assertEquals(expectedCount, basicNumericDatasetRanges.getDimensionCount());

    for (int i = 0; i < basicNumericDatasetRanges.getDimensionCount(); i++) {
      Assert.assertEquals(expectedCentroidValues[i], centroid[i], DELTA);
    }
  }

  @Test
  public void testNumericValuesAllValues() {

    final int expectedCount = 3;

    final double[] expectedValues = new double[] {25, 60, 0};

    final double[] mins = basicNumericDatasetValues.getMinValuesPerDimension();
    final double[] max = basicNumericDatasetValues.getMaxValuesPerDimension();
    final double[] centroid = basicNumericDatasetValues.getCentroidPerDimension();

    Assert.assertEquals(expectedCount, basicNumericDatasetValues.getDimensionCount());

    for (int i = 0; i < basicNumericDatasetValues.getDimensionCount(); i++) {
      Assert.assertEquals(expectedValues[i], mins[i], DELTA);
      Assert.assertEquals(expectedValues[i], max[i], DELTA);
      Assert.assertEquals(expectedValues[i], centroid[i], DELTA);
    }
  }
}
