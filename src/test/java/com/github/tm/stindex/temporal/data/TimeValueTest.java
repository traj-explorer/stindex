package com.github.tm.stindex.temporal.data;

import org.junit.Test;

public class TimeValueTest {

  @Test
  public void timeValueTest() {
    TimeValue time1 = new TimeValue(2020, TimeGranularity.YEAR);
    System.out.println(time1);

    TimeValue time2 = new TimeValue(2020, 1, TimeGranularity.MONTH);
    System.out.println(time2);

    TimeValue time3 = new TimeValue(2020, 1, 18, TimeGranularity.DAY);
    System.out.println(time3);

    TimeValue time4 = new TimeValue(2020, 1, 18, 12, TimeGranularity.HOUR);
    System.out.println(time4);

    TimeValue time5 = new TimeValue(2020, 1, 18, 12, 12, TimeGranularity.MINUTE);
    System.out.println(time5);

    TimeValue time6 = new TimeValue(2020, 1, 18, 12, 12, 12);
    System.out.println(time6);

    TimeValue time7 = new TimeValue(1, TimeGranularity.MONTH);
    System.out.println(time7);

    TimeValue time8 = new TimeValue(1, 18, TimeGranularity.DAY);
    System.out.println(time8);

    TimeValue time9 = new TimeValue(1, 18, 12, TimeGranularity.HOUR);
    System.out.println(time9);

    TimeValue time10 = new TimeValue(1, 18, 12, 12, TimeGranularity.MINUTE);
    System.out.println(time10);

    TimeValue time11 = new TimeValue(1, 18, 12, 12, 12, TimeGranularity.SECOND);
    System.out.println(time11);
  }

}