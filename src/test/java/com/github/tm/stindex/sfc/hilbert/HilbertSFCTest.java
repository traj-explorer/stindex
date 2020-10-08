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
package com.github.tm.stindex.sfc.hilbert;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.spatial.GridIndex;
import com.github.tm.stindex.PrintUtils;
import com.github.tm.stindex.dimension.BasicDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.RangeDecomposition;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import com.github.tm.stindex.spatial.sfc.hilbert.HilbertSFC;
import org.junit.Before;
import org.junit.Test;

public class HilbertSFCTest {

  private GridIndex gridIndex;
  private int res = 20;

  @Before
  public void setUp() {
    final SFCDimensionDefinition[] dimensions = {
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), res),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), res)};
    gridIndex = new HilbertSFC(dimensions);
  }

  @Test
  public void printGlobalIndexTest() {
    PrintUtils.printGridIndexValues(gridIndex, res, res);
  }

  @Test
  public void getIndexTest() {
    ByteArray index = gridIndex.getIndex(new double[] {90, 0});
    System.out.println(index);
    System.out.println(index.getRes());
  }

  @Test
  public void getRangeTest() {
    ByteArray index = gridIndex.getIndex(new double[] {90, 0});
    MultiDimensionalNumericData range = gridIndex.getRanges(index);
    System.out.println(range);
  }

  @Test
  public void indexLengthTest() {
    ByteArray index = gridIndex.getIndex(new double[] {-45, -20});
    System.out.println(index.length());
  }

  @Test
  public void decomposeRangeTest() {
    NumericRange lonRange = new NumericRange(-90, 90);
    NumericRange latRange = new NumericRange(-90, 0);
    BasicNumericDataset dataset = new BasicNumericDataset(new NumericData[] {lonRange, latRange});
    RangeDecomposition rangeDecomposition = gridIndex.decomposeRangeFully(dataset);
    for (ByteArrayRange range : rangeDecomposition.getRanges()) {
      System.out.println(range);
    }
  }

  @Test
  public void getCoordinatesTest() {
    ByteArray index = gridIndex.getIndex(new double[] {-45, -20});
    System.out.println(index);
    long[] coordinates = gridIndex.getCoordinates(index);
    for (long c : coordinates) {
      System.out.println(c);
    }
  }

  @Test
  public void getFatherRangeTest() {
    ByteArray start = gridIndex.getIndex(new double[] {-180, -90});
    ByteArray end = gridIndex.getIndex(new double[] {-180, 0});
    ByteArrayRange childrenRange = new ByteArrayRange(start, end);
    ByteArrayRange fatherRange = gridIndex.getFatherRange(childrenRange);
    System.out.println("children range: " + childrenRange + ", children res: " + childrenRange.getStart().getRes());
    System.out.println("father range: " + fatherRange + ", father res: " + fatherRange.getStart().getRes());
  }

  @Test
  public void getRing() {
    ByteArray index = gridIndex.getIndex(new double[] {-45, 20});
    ByteArrayRange childrenRange = new ByteArrayRange(index, index);
    System.out.println("children:" + childrenRange);
    ByteArrayRange father = childrenRange;
    for (int i = 0; i < 10; i++) {
      father = gridIndex.getFatherRange(father);
    }
    System.out.println("father: " + father);
    long start = System.currentTimeMillis();
    ByteArrayRange[] ring = gridIndex.getRing(father);
    long end = System.currentTimeMillis();
    System.out.println("time: " + (end - start));
    for (ByteArrayRange range : ring) {
      System.out.println(range);
    }
  }
}