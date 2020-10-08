package com.github.tm.stindex.temporal;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.temporal.data.TimeData;
import com.github.tm.stindex.temporal.data.TimeGranularity;
import com.github.tm.stindex.temporal.data.TimePeriod;
import com.github.tm.stindex.temporal.data.TimeValue;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimePeriodDimensionDefinitionTest {

  private TimePeriodDimensionDefinition dimDef = new TimePeriodDimensionDefinition(Calendar.DAY_OF_MONTH);

  @Test
  public void normalizeTest() {
    TimeValue dateTime = new TimeValue(2020, 1, 31, 16, 51, 16);
    ByteArrayRange timeIndex = dimDef.normalize(dateTime);
    System.out.println(timeIndex);
  }

  @Test
  public void decomposeRangeTest() {
    TimeValue upperStartDateTime = new TimeValue(2015, 5, 29, TimeGranularity.DAY);
    TimeValue upperEndDateTime = new TimeValue(2015, 6, 1, TimeGranularity.DAY);
    TimeValue bottomStartDateTime = new TimeValue( 10, 23, 0, TimeGranularity.SECOND);
    TimeValue bottomEndDateTime = new TimeValue(11, 59, 59, TimeGranularity.SECOND);
    TimeData data = new TimePeriod(upperStartDateTime, upperEndDateTime,
                                   bottomStartDateTime, bottomEndDateTime);
    ByteArrayRange[] ranges = dimDef.decomposeRange(data);
    for (ByteArrayRange range : ranges) {
      System.out.println(range);
    }
  }

  @Test
  public void getBinWithStartEndTest() {
    TimeValue upperStartDateTime = new TimeValue(2015, 5, 29, TimeGranularity.DAY);
    TimeValue upperEndDateTime = new TimeValue(2015, 6, 1, TimeGranularity.DAY);
    TimeValue bottomStartDateTime = new TimeValue( 10, 23, 0, TimeGranularity.SECOND);
    TimeValue bottomEndDateTime = new TimeValue(11, 59, 59, TimeGranularity.SECOND);
    TimeData data = new TimePeriod(upperStartDateTime, upperEndDateTime,
                                   bottomStartDateTime, bottomEndDateTime);
    HashMap<ByteArray, ByteArrayRange> bins = dimDef.getBinWithStartEnd(data);

    for (Map.Entry<ByteArray, ByteArrayRange> entry : bins.entrySet()) {
      System.out.println(entry.getKey() + ", " + entry.getValue());
    }
  }
}