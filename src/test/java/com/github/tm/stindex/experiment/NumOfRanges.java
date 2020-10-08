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
package com.github.tm.stindex.experiment;

import com.github.tm.stindex.spatial.GridIndex;
import com.github.tm.stindex.RandomUtils;
import com.github.tm.stindex.dimension.BasicDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.RangeDecomposition;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.SFCFactory;
import com.github.tm.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import com.github.tm.stindex.spatial.sfc.zorder.ZOrderSFC;

/**
 * @author Yu Liebing
 */
public class NumOfRanges {

  private GridIndex squareHilbert;
  private GridIndex rectHilbert;
  private GridIndex zOrder;

  public NumOfRanges(int res) {
    SFCDimensionDefinition[] unbalanceDimDef = new SFCDimensionDefinition[] {
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), res),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), res / 2)
    };
    SFCDimensionDefinition[] balanceDimDef = new SFCDimensionDefinition[] {
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), res),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), res)
    };
    squareHilbert = SFCFactory.createSpaceFillingCurve(balanceDimDef, SFCFactory.SFCType.HILBERT);
    rectHilbert = SFCFactory.createSpaceFillingCurve(unbalanceDimDef, SFCFactory.SFCType.HILBERT);
    zOrder = SFCFactory.createSpaceFillingCurve(balanceDimDef, SFCFactory.SFCType.ZORDER);
  }

  public int runAvgNumOfRanges(int times, double dimDiff) {
    long squareNum = 0L;
    long rectNum = 0L;
    long zOrderNum = 0L;
    long squareTime = 0L;
    long rectTime = 0L;
    long zOrderTime = 0L;
    for (int i = 0; i < times; i++) {
      double minLon = RandomUtils.randomLon();
      double maxLon = minLon + dimDiff > 180.0 ? 180.0 : minLon + dimDiff;
      double minLat = RandomUtils.randomLat();
      double maxLat = minLat + dimDiff > 90.0 ? 90.0 : minLat + dimDiff;
//      System.out.println("[" + minLon + ", " + maxLon + "], [" + minLat + ", " + maxLat + "]");
      NumericRange lonRange = new NumericRange(minLon, maxLon);
      NumericRange latRange = new NumericRange(minLat, maxLat);
      BasicNumericDataset dataset = new BasicNumericDataset(new NumericData[] {lonRange, latRange});
      long t1 = System.currentTimeMillis();
      RangeDecomposition squareRes = squareHilbert.decomposeRangeFully(dataset);
      long t2 = System.currentTimeMillis();
      RangeDecomposition rectRes = rectHilbert.decomposeRangeFully(dataset);
      long t3 = System.currentTimeMillis();
      RangeDecomposition zOrderRes = ((ZOrderSFC)zOrder).decomposeRange(dataset, 20);
      long t4 = System.currentTimeMillis();
      squareNum += squareRes.getRanges().length;
      rectNum += rectRes.getRanges().length;
      zOrderNum += zOrderRes.getRanges().length;
      squareTime += t2 - t1;
      rectTime += t3 - t2;
      zOrderNum += t4 - t3;
    }
    squareNum /= times;
    rectNum /= times;
    zOrderNum /= times;
    System.out.println(squareNum);
    System.out.println(rectNum);
    System.out.println(zOrderNum);

    System.out.println(squareTime);
    System.out.println(rectTime);
    System.out.println(zOrderTime);
    return 0;
  }

  public static void main(String[] args) {
    NumOfRanges test = new NumOfRanges(20);
    test.runAvgNumOfRanges(1000, 0.05);
  }
}
