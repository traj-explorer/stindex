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
package com.github.tm.stindex.sfc.zorder;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.PrintUtils;
import com.github.tm.stindex.dimension.BasicDimensionDefinition;
import com.github.tm.stindex.spatial.GridIndex;
import com.github.tm.stindex.spatial.sfc.RangeDecomposition;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import com.github.tm.stindex.spatial.sfc.zorder.ZOrderSFC;
import org.junit.Before;
import org.junit.Test;

public class ZOrderSFCTest {

  GridIndex gridIndex;
  int res = 2;

  @Before
  public void setUp() {
    final SFCDimensionDefinition[] dimensions = {
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), res),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), res)};
    gridIndex = new ZOrderSFC(dimensions);
  }

  @Test
  public void printGlobalIndexTest() {
    PrintUtils.printGridIndexValues(gridIndex, res);
  }

  @Test
  public void getIndexTest() {
    ByteArray index = gridIndex.getIndex(new double[] {-45, -20});
    System.out.println(index.toLong());
  }

  @Test
  public void getChildrenRangeTest() {
    ByteArray commonPrefix = new ByteArray(new byte[] {0});
    ByteArrayRange childRange = ((ZOrderSFC)gridIndex).getChildrenRange(commonPrefix, 4, 2, 3);
    System.out.println(childRange);
  }

  @Test
  public void decomposeRangeTest() {
    NumericRange lonRange = new NumericRange(-180, 180);
    NumericRange latRange = new NumericRange(-90, 44);
    BasicNumericDataset dataset = new BasicNumericDataset(new NumericData[] {lonRange, latRange});
    RangeDecomposition ranges = ((ZOrderSFC)gridIndex).decomposeRange(dataset, 10);
    for (ByteArrayRange range : ranges.getRanges()) {
      System.out.println(range);
    }
  }
}
