package com.github.tm.stindex.api;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.dimension.BasicDimensionDefinition;
import com.github.tm.stindex.dimension.TimeDimensionDefinition;
import com.github.tm.stindex.spatial.GridIndex;
import com.github.tm.stindex.spatial.h3.H3Index;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.SFCFactory;
import com.github.tm.stindex.st.ConcatenationEncoding;
import com.github.tm.stindex.st.STEncoding;
import com.github.tm.stindex.temporal.ConcatenationTimeEncoding;
import com.github.tm.stindex.temporal.TimeEncoding;
import com.github.tm.stindex.temporal.TimePeriodDimensionDefinition;
import com.github.tm.stindex.temporal.TimePointDimensionDefinition;
import com.github.tm.stindex.temporal.data.TimeValue;

/**
 * @author Yu Liebing
 */
public class STIndex {
  private STEncoding stEncoding;
  private SpatialIndexType spatialIndexType;
  private int spatialRes;
  private TemporalIndexType temporalIndexType;
  private int temporalRes;

  public STIndex(final TemporalIndexType temporalIndexType, final int temporalRes,
                 final SpatialIndexType spatialIndexType, final int spatialRes) {
    this.temporalIndexType = temporalIndexType;
    this.temporalRes = temporalRes;
    this.spatialIndexType = spatialIndexType;
    this.spatialRes = spatialRes;

    TimeDimensionDefinition timeDimDef = null;
    if (temporalIndexType == TemporalIndexType.TIME_POINT) {
      timeDimDef = new TimePointDimensionDefinition(temporalRes);
    } else {
      timeDimDef = new TimePeriodDimensionDefinition(temporalRes);
    }
    TimeEncoding timeEncoding = new ConcatenationTimeEncoding(timeDimDef);

    GridIndex gridIndex = null;
    final SFCDimensionDefinition[] dimensions = {
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), spatialRes),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), spatialRes)};
    switch (spatialIndexType) {
      case ZOrder:
        gridIndex = SFCFactory.createSpaceFillingCurve(dimensions, SFCFactory.SFCType.ZORDER);
        break;
      case Hilbert:
        gridIndex = SFCFactory.createSpaceFillingCurve(dimensions, SFCFactory.SFCType.HILBERT);
        break;
      case XZOrder:
        gridIndex = SFCFactory.createSpaceFillingCurve(dimensions, SFCFactory.SFCType.XZORDER);
        break;
      case H3:
        gridIndex = new H3Index(spatialRes);
        break;
    }

    stEncoding = new ConcatenationEncoding(timeEncoding, gridIndex);
  }

  public ByteArray getIndex(TimeValue time, double lon, double lat) {
    return stEncoding.getIndex(time, new double[]{lon, lat});
  }

  public ByteArrayRange[] decomposeRange() {
    return null;
  }

  public ByteArrayRange getSpatialFatherRange() {
    return null;
  }

  public ByteArrayRange[] getRing() {
    return null;
  }

  public BoundingBox getSTBoundingBox(ByteArray index) {
    return null;
  }

  public SpatialIndexType getSpatialIndexType() {
    return spatialIndexType;
  }

  public int getSpatialRes() {
    return spatialRes;
  }

  public TemporalIndexType getTemporalIndexType() {
    return temporalIndexType;
  }

  public int getTemporalRes() {
    return temporalRes;
  }
}
