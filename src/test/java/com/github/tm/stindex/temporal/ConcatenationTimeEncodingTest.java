package com.github.tm.stindex.temporal;

import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.dimension.TimeDimensionDefinition;
import com.github.tm.stindex.temporal.data.TimeValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

public class ConcatenationTimeEncodingTest {

  private ConcatenationTimeEncoding pointTimeEncoding;
  private ConcatenationTimeEncoding periodTimeEncoding;

  @Before
  public void setUp() {
    TimeDimensionDefinition pointTimeDimDef = new TimePointDimensionDefinition(Calendar.HOUR);
    TimeDimensionDefinition periodTimeDimDef = new TimePeriodDimensionDefinition(Calendar.DATE);

    pointTimeEncoding = new ConcatenationTimeEncoding(pointTimeDimDef);
    periodTimeEncoding = new ConcatenationTimeEncoding(periodTimeDimDef);
  }

  @Test
  public void getIndex() {
    TimeValue time = new TimeValue(2020, 3, 11, 20, 42, 55);
    System.out.println(pointTimeEncoding.getIndex(time));
    System.out.println(periodTimeEncoding.getIndex(time));
  }

  @Test
  public void getTemporalRange() {
    TimeValue time = new TimeValue(2020, 3, 11, 20, 42, 55);

    ByteArrayRange pointRange = pointTimeEncoding.getIndex(time);
    System.out.println(pointTimeEncoding.getTemporalRange(pointRange));

    ByteArrayRange periodRange = periodTimeEncoding.getIndex(time);
    System.out.println(periodTimeEncoding.getTemporalRange(periodRange));
  }

  @Test
  public void decomposeRange() {
  }
}