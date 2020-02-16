package com.github.stdi_sys.stindex.st;

import com.github.stdi_sys.stindex.ByteArray;
import com.github.stdi_sys.stindex.ByteArrayRange;
import com.github.stdi_sys.stindex.STByteArray;
import com.github.stdi_sys.stindex.spatial.GridIndex;
import com.github.stdi_sys.stindex.spatial.sfc.RangeDecomposition;
import com.github.stdi_sys.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.github.stdi_sys.stindex.temporal.TimeEncoding;
import com.github.stdi_sys.stindex.temporal.TimePeriodDimensionDefinition;
import com.github.stdi_sys.stindex.temporal.TimePointDimensionDefinition;
import com.github.stdi_sys.stindex.temporal.data.TimeData;
import com.github.stdi_sys.stindex.temporal.data.TimeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yu Liebing
 */
public class ConcatenationEncoding implements STEncoding {
  private TimeEncoding concatenationTimeEncoding;
  private GridIndex gridIndex;

  public ConcatenationEncoding(final TimeEncoding concatenationTimeEncoding,
                               final GridIndex gridIndex) {
    this.concatenationTimeEncoding = concatenationTimeEncoding;
    this.gridIndex = gridIndex;
  }

  @Override
  public ByteArray getIndex(TimeValue time, double[] values) {
    ByteArrayRange temporalIndex = concatenationTimeEncoding.getIndex(time);
    ByteArray spatialIndex = gridIndex.getIndex(values);
    if (temporalIndex.isSingleValue()) {
      return new STByteArray(temporalIndex.getStart(), spatialIndex);
    } else {
      return new STByteArray(temporalIndex.getStart(), spatialIndex, temporalIndex.getEnd());
    }
  }

  @Override
  public ByteArrayRange[] decomposeRanges(TimeData queryTime, MultiDimensionalNumericData querySpatial) {
    RangeDecomposition spatialRanges = gridIndex.decomposeRangeFully(querySpatial);
    ByteArrayRange[] stRanges;
    if (concatenationTimeEncoding.getTimeDimensionDefinition() instanceof TimePointDimensionDefinition) {
      ByteArrayRange[] temporalRanges = concatenationTimeEncoding.decomposeRange(queryTime);
      stRanges = new ByteArrayRange[temporalRanges.length * spatialRanges.getRanges().length];
      int i = 0;
      for (ByteArrayRange temporalRange : temporalRanges) {
        for (ByteArrayRange spatialRange : spatialRanges.getRanges()) {
          ByteArray start = new STByteArray(temporalRange.getStart(), spatialRange.getStart());
          ByteArray end = new STByteArray(temporalRange.getStart(), spatialRange.getEnd());
          stRanges[i++] = new ByteArrayRange(start, end);
        }
      }
    } else {
      TimePeriodDimensionDefinition dimDef =
              (TimePeriodDimensionDefinition) concatenationTimeEncoding.getTimeDimensionDefinition();
      HashMap<ByteArray, ByteArrayRange> binWithStartEnd = dimDef.getBinWithStartEnd(queryTime);
      stRanges = new ByteArrayRange[binWithStartEnd.size() * spatialRanges.getRanges().length];
      int i = 0;
      for (Map.Entry<ByteArray, ByteArrayRange> entry : binWithStartEnd.entrySet()) {
        for (ByteArrayRange spatialRange : spatialRanges.getRanges()) {
          ByteArray start = new STByteArray(entry.getKey(), spatialRange.getStart(), entry.getValue().getStart());
          ByteArray end = new STByteArray(entry.getKey(), spatialRange.getEnd(), entry.getValue().getEnd());
          stRanges[i++] = new ByteArrayRange(start, end);
        }
      }
    }
    return stRanges;
  }

  @Override
  public ByteArrayRange getSpatialFather(ByteArrayRange index) {
    return null;
  }

  @Override
  public ByteArrayRange[] getSpatialRing(ByteArrayRange index) {
    return new ByteArrayRange[0];
  }
}
