package com.github.tm.stindex.temporal;

import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.temporal.data.TimeData;
import com.github.tm.stindex.temporal.data.TimeRange;
import com.github.tm.stindex.temporal.data.TimeValue;
import org.junit.Test;

import java.util.Calendar;

public class TimePointDimensionDefinitionTest {
  private TimePointDimensionDefinition dimDef = new TimePointDimensionDefinition(Calendar.HOUR);

  @Test
  public void normalizeTest() {
    TimeValue time = new TimeValue(2020, 1, 31, 16, 45, 8);
    ByteArrayRange index = dimDef.normalize(time);
    System.out.println(index);
  }

  @Test
  public void decomposeRange() {
    TimeValue startDateTime = new TimeValue(2020, 1, 1, 10, 12, 13);
    TimeValue endDateTime = new TimeValue(2020, 1, 2, 10, 2, 13);
    TimeData timeData = new TimeRange(startDateTime, endDateTime);
    ByteArrayRange[] ranges = dimDef.decomposeRange(timeData);
    for (ByteArrayRange range : ranges) {
      System.out.println(range);
    }
  }
}