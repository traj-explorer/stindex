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
package com.github.tm.stindex.temporal;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.TemporalByteArray;
import com.github.tm.stindex.dimension.TimeDimensionDefinition;
import com.github.tm.stindex.temporal.data.TimeData;
import com.github.tm.stindex.temporal.data.TimeGranularity;
import com.github.tm.stindex.temporal.data.TimeRange;
import com.github.tm.stindex.temporal.data.TimeValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Yu Liebing
 */
public class TimePointDimensionDefinition implements TimeDimensionDefinition {
  private int minGranularity;

  public TimePointDimensionDefinition(final int minGranularity) {
    this.minGranularity = minGranularity;
  }

  @Override
  public ByteArrayRange normalize(TimeValue time) {
    List<Byte> bytes = new ArrayList<>(7);
    if (minGranularity >= Calendar.YEAR) {
      int year = time.getYear();
      byte y1 = (byte) (year / 100);
      byte y2 = (byte) (year % 100);
      bytes.add(y1);
      bytes.add(y2);
    }
    if (minGranularity >= Calendar.MONTH) {
      int month = time.getMonthValue();
      bytes.add((byte)month);
    }
    if (minGranularity >= Calendar.DAY_OF_MONTH) {
      int day = time.getDayOfMonth();
      bytes.add((byte)day);
    }
    if (minGranularity >= Calendar.HOUR) {
      int hour = time.getHour();
      bytes.add((byte)hour);
    }
    if (minGranularity >= Calendar.MINUTE) {
      int minute = time.getMinute();
      bytes.add((byte)minute);
    }
    if (minGranularity >= Calendar.SECOND) {
      int second = time.getSecond();
      bytes.add((byte)second);
    }
    ByteArray index = new TemporalByteArray(bytes);
    return new ByteArrayRange(index, index, true);
  }

  @Override
  public TimeValue denormalize(ByteArrayRange index) {
    if (!index.isSingleValue()) {
      throw new IllegalArgumentException("Time point index only support for Single value.");
    }
    ByteArray value = index.getStart();
    if (minGranularity == Calendar.YEAR) {
      if (value.getBytes().length == 2) {
        int year = value.getBytes()[0] * 100 + value.getBytes()[1];
        return new TimeValue(year, TimeGranularity.YEAR);
      }
    }
    if (minGranularity == Calendar.MONTH) {
      if (value.getBytes().length == 3) {
        int year = value.getBytes()[0] * 100 + value.getBytes()[1];
        int month = value.getBytes()[2];
        return new TimeValue(year, month, TimeGranularity.MONTH);
      }
    }
    if (minGranularity == Calendar.DAY_OF_MONTH) {
      if (value.getBytes().length == 4) {
        int year = value.getBytes()[0] * 100 + value.getBytes()[1];
        int month = value.getBytes()[2];
        int day = value.getBytes()[3];
        return new TimeValue(year, month, day, TimeGranularity.DAY);
      }
    }
    if (minGranularity == Calendar.HOUR) {
      if (value.getBytes().length == 5) {
        int year = value.getBytes()[0] * 100 + value.getBytes()[1];
        int month = value.getBytes()[2];
        int day = value.getBytes()[3];
        int hour = value.getBytes()[4];
        return new TimeValue(year, month, day, hour, TimeGranularity.HOUR);
      }
    }
    if (minGranularity == Calendar.MINUTE) {
      if (value.getBytes().length == 6) {
        int year = value.getBytes()[0] * 100 + value.getBytes()[1];
        int month = value.getBytes()[2];
        int day = value.getBytes()[3];
        int hour = value.getBytes()[4];
        int minute = value.getBytes()[5];
        return new TimeValue(year, month, day, hour, minute, TimeGranularity.MINUTE);
      }
    }
    if (minGranularity == Calendar.SECOND) {
      if (value.getBytes().length == 7) {
        int year = value.getBytes()[0] * 100 + value.getBytes()[1];
        int month = value.getBytes()[2];
        int day = value.getBytes()[3];
        int hour = value.getBytes()[4];
        int minute = value.getBytes()[5];
        int second = value.getBytes()[6];
        return new TimeValue(year, month, day, hour, minute, second);
      }
    }
    throw new IllegalArgumentException("Value length not match.");
  }

  @Override
  public ByteArrayRange[] decomposeRange(TimeData query) {
    if (!(query instanceof TimeRange)) {
      throw new IllegalArgumentException("TimePointDimensionDefinition only support TimeRange.");
    }
    TimeValue startTime = query.getStartTime();
    TimeValue endTime = query.getEndTime();
    List<TimeValue> times = new ArrayList<>();
    times.add(startTime);
    TimeValue tmpDateTime = startTime;
    while (tmpDateTime.compareTo(endTime) < 0) {
      if (minGranularity == Calendar.YEAR) {
        tmpDateTime = tmpDateTime.plusYears(1);
      } else if (minGranularity == Calendar.MONTH) {
        tmpDateTime = tmpDateTime.plusMonths(1);
      } else if (minGranularity == Calendar.DAY_OF_MONTH) {
        tmpDateTime = tmpDateTime.plusDays(1);
      } else if (minGranularity == Calendar.HOUR) {
        tmpDateTime = tmpDateTime.plusHours(1);
      } else if (minGranularity == Calendar.MINUTE) {
        tmpDateTime = tmpDateTime.plusMinutes(1);
      } else if (minGranularity == Calendar.SECOND) {
        tmpDateTime = tmpDateTime.plusSeconds(1);
      }
      if (tmpDateTime.compareTo(endTime) < 0) {
        times.add(tmpDateTime);
      }
    }

    ByteArrayRange[] timeRanges = new ByteArrayRange[times.size()];
    for (int i = 0; i < timeRanges.length; i++) {
      timeRanges[i] = normalize(times.get(i));
    }
    return timeRanges;
  }
}