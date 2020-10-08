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

/**
 * @author Yu Liebing
 * Create on 2020-01-21.
 */
public class Hilbert3D {

  private GridIndex hilbert;

  public Hilbert3D(int res) {
    SFCDimensionDefinition[] dimDef = new SFCDimensionDefinition[] {
            new SFCDimensionDefinition(new BasicDimensionDefinition(0, 512), 9),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), res),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), res)
    };
    hilbert = SFCFactory.createSpaceFillingCurve(dimDef, SFCFactory.SFCType.HILBERT);
  }

  public void run() {
    double dimDiff = 0.05;
    double minLon = RandomUtils.randomLon();
    double maxLon = minLon + dimDiff > 180.0 ? 180.0 : minLon + dimDiff;
    double minLat = RandomUtils.randomLat();
    double maxLat = minLat + dimDiff > 90.0 ? 90.0 : minLat + dimDiff;
//      System.out.println("[" + minLon + ", " + maxLon + "], [" + minLat + ", " + maxLat + "]");
    NumericRange timeRange = new NumericRange(1, 2);
    NumericRange lonRange = new NumericRange(minLon, maxLon);
    NumericRange latRange = new NumericRange(minLat, maxLat);
    BasicNumericDataset dataset = new BasicNumericDataset(new NumericData[] {timeRange, lonRange, latRange});
    RangeDecomposition rangeDecomposition = hilbert.decomposeRangeFully(dataset);
    System.out.println(rangeDecomposition.getRanges().length);
  }

  public static void main(String[] args) {
    new Hilbert3D(10).run();
  }
}
