package com.github.stdi_sys.stindex.st;

import com.github.stdi_sys.stindex.ByteArray;
import com.github.stdi_sys.stindex.ByteArrayRange;
import com.github.stdi_sys.stindex.dimension.BasicDimensionDefinition;
import com.github.stdi_sys.stindex.dimension.TimeDimensionDefinition;
import com.github.stdi_sys.stindex.spatial.GridIndex;
import com.github.stdi_sys.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.stdi_sys.stindex.spatial.sfc.SFCFactory;
import com.github.stdi_sys.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericData;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericRange;
import com.github.stdi_sys.stindex.temporal.ConcatenationTimeEncoding;
import com.github.stdi_sys.stindex.temporal.TimeEncoding;
import com.github.stdi_sys.stindex.temporal.TimePeriodDimensionDefinition;
import com.github.stdi_sys.stindex.temporal.TimePointDimensionDefinition;
import com.github.stdi_sys.stindex.temporal.data.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

public class ConcatenationEncodingTest {
  private STEncoding timePointEncoding;
  private STEncoding timePeriodEncoding;

  @Before
  public void setUp() {
    TimeDimensionDefinition timePointDimDef = new TimePointDimensionDefinition(Calendar.HOUR);
    TimeEncoding timePointTimeEncoding = new ConcatenationTimeEncoding(timePointDimDef);

    TimeDimensionDefinition timePeriodDimDef = new TimePeriodDimensionDefinition(Calendar.DAY_OF_MONTH);
    TimeEncoding timePeriodTimeEncoding = new ConcatenationTimeEncoding(timePeriodDimDef);

    SFCDimensionDefinition[] dimensions = {
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), 2),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), 2)};
    GridIndex hilbert = SFCFactory.createSpaceFillingCurve(dimensions, SFCFactory.SFCType.HILBERT);

    timePointEncoding = new ConcatenationEncoding(timePointTimeEncoding, hilbert);
    timePeriodEncoding = new ConcatenationEncoding(timePeriodTimeEncoding, hilbert);
  }

  @Test
  public void getIndexTest() {
    TimeValue time = new TimeValue(2020, 1, 31, 18,48, 18);
    double[] lonLat = new double[] {114.35, 35.50};

    ByteArray index1 = timePointEncoding.getIndex(time, lonLat);
    System.out.println(index1);

    ByteArray index2 = timePeriodEncoding.getIndex(time, lonLat);
    System.out.println(index2);
  }

  @Test
  public void decomposeRangeTest() {
    NumericRange lonRange = new NumericRange(-180, 0.0);
    NumericRange latRange = new NumericRange(-44.9, 45);
    BasicNumericDataset spatialRange = new BasicNumericDataset(new NumericData[] {lonRange, latRange});
    // time point index decompose
    TimeValue startTime = new TimeValue(2020, 1, 1, 18,48, 18);
    TimeValue endTime = new TimeValue(2020, 1, 1, 18,59, 18);
    TimeData timePointRange = new TimeRange(startTime, endTime);
    ByteArrayRange[] timePointRanges = timePointEncoding.decomposeRanges(timePointRange, spatialRange);
    for (ByteArrayRange range : timePointRanges) {
      System.out.println(range);
    }

    System.out.println("--------");
    // time period index decompose
    TimeValue upperStartDateTime = new TimeValue(2015, 5, 1, TimeGranularity.DAY);
    TimeValue upperEndDateTime = new TimeValue(2015, 5, 30, TimeGranularity.DAY);
    TimeValue bottomStartDateTime = new TimeValue( 10, 23, 0, TimeGranularity.SECOND);
    TimeValue bottomEndDateTime = new TimeValue(10, 59, 59, TimeGranularity.SECOND);
    TimeData timePeriodRange = new TimePeriod(upperStartDateTime, upperEndDateTime,
                                              bottomStartDateTime, bottomEndDateTime);
    ByteArrayRange[] timePeriodRanges = timePeriodEncoding.decomposeRanges(timePeriodRange, spatialRange);
    for (ByteArrayRange range : timePeriodRanges) {
      System.out.println(range);
    }
  }
}