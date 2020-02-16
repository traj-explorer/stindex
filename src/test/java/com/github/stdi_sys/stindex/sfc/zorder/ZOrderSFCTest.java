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
package com.github.stdi_sys.stindex.sfc.zorder;

import com.github.stdi_sys.stindex.*;
import com.github.stdi_sys.stindex.dimension.BasicDimensionDefinition;
import com.github.stdi_sys.stindex.spatial.GridIndex;
import com.github.stdi_sys.stindex.spatial.sfc.RangeDecomposition;
import com.github.stdi_sys.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.stdi_sys.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericData;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericRange;
import com.github.stdi_sys.stindex.spatial.sfc.zorder.ZOrderSFC;
import com.google.common.primitives.Ints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;

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

//  @Test
//  public void getFatherTest() {
//    for (int i = 0; i < 1000; i++) {
//      double lon = RandomUtils.randomLon();
//      double lat = RandomUtils.randomLat();
//      ByteArray index = gridIndex.getIndex(new double[] {lon, lat});
//      ByteArray father = gridIndex.getFather(index);
//      Assert.assertEquals(index.toLong() / 4, father.toLong());
//    }
//  }
//
//  @Test
//  public void getChildrenTest() {
//    for (int i = 0; i < 1000; i++) {
//      double lon = RandomUtils.randomLon();
//      double lat = RandomUtils.randomLat();
//      ByteArray index = gridIndex.getIndex(new double[] {lon, lat});
//      ByteArrayRange children = gridIndex.getChildren(index);
//      Assert.assertEquals(children.getStart().toLong(), index.toLong() * 4);
//      Assert.assertEquals(children.getEnd().toLong(), index.toLong() * 4 + 3);
//    }
//  }

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

  @Test
  public void temp() {
    BitSet bitSet = BitSet.valueOf(ByteArrayUtils.swapEndianFormat(Ints.toByteArray(2)));
    PrintUtils.printBitset(bitSet);
  }
}
