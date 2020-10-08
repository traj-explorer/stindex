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
import com.github.tm.stindex.temporal.data.TimePeriod;
import com.github.tm.stindex.temporal.data.TimeValue;

import java.util.*;

/**
 * @author Yu Liebing
 */
public class TimePeriodDimensionDefinition implements TimeDimensionDefinition {
  private int upperGranularity;

  public TimePeriodDimensionDefinition(final int upperGranularity) {
    this.upperGranularity = upperGranularity;
  }

  @Override
  public ByteArrayRange normalize(TimeValue time) {
    List<Byte> frontBytes = new ArrayList<>(6);
    List<Byte> backBytes = new ArrayList<>(2);
    if (upperGranularity == Calendar.YEAR) {
      int year = time.getYear();
      int month = time.getMonthValue();
      frontBytes.add((byte)month);
      backBytes.add((byte)(year / 100));
      backBytes.add((byte)(year % 100));
    } else if (upperGranularity == Calendar.MONTH) {
      int year = time.getYear();
      int month = time.getMonthValue();
      int day = time.getDayOfMonth();
      frontBytes.add((byte)(year / 100));
      frontBytes.add((byte)(year % 100));
      frontBytes.add((byte)day);
      backBytes.add((byte)month);
    } else if (upperGranularity == Calendar.DAY_OF_MONTH) {
      int year = time.getYear();
      int month = time.getMonthValue();
      int day = time.getDayOfMonth();
      int hour = time.getHour();
      frontBytes.add((byte)(year / 100));
      frontBytes.add((byte)(year % 100));
      frontBytes.add((byte)month);
      frontBytes.add((byte)hour);
      backBytes.add((byte)day);
    } else if (upperGranularity == Calendar.HOUR) {
      int year = time.getYear();
      int month = time.getMonthValue();
      int day = time.getDayOfMonth();
      int hour = time.getHour();
      int minute = time.getMinute();
      frontBytes.add((byte)(year / 100));
      frontBytes.add((byte)(year % 100));
      frontBytes.add((byte)month);
      frontBytes.add((byte)day);
      frontBytes.add((byte)minute);
      backBytes.add((byte)hour);
    } else if (upperGranularity == Calendar.MINUTE) {
      int year = time.getYear();
      int month = time.getMonthValue();
      int day = time.getDayOfMonth();
      int hour = time.getHour();
      int minute = time.getMinute();
      int second = time.getSecond();
      frontBytes.add((byte)(year / 100));
      frontBytes.add((byte)(year % 100));
      frontBytes.add((byte)month);
      frontBytes.add((byte)day);
      frontBytes.add((byte)hour);
      frontBytes.add((byte)second);
      backBytes.add((byte)minute);
    }

    return new ByteArrayRange(new TemporalByteArray(frontBytes), new TemporalByteArray(backBytes));
  }

  @Override
  public TimeValue denormalize(ByteArrayRange value) {
    byte[] start = value.getStart().getBytes();
    byte[] end = value.getEnd().getBytes();
    if (upperGranularity == Calendar.YEAR) {
      if (start.length == 1 && end.length == 2) {
        int year = end[0] * 100 + end[1];
        int month = start[0];
        return new TimeValue(year, month, TimeGranularity.MONTH);
      }
    } else if (upperGranularity == Calendar.MONTH) {
      if (start.length == 3 && end.length == 1) {
        int year = start[0] * 100 + start[1];
        int day = start[2];
        int month = end[0];
        return new TimeValue(year, month, day, TimeGranularity.DAY);
      }
    } else if (upperGranularity == Calendar.DAY_OF_MONTH) {
      if (start.length == 4 && end.length == 1) {
        int year = start[0] * 100 + start[1];
        int month = start[2];
        int hour = start[3];
        int day = end[0];
        return new TimeValue(year, month, day, hour, TimeGranularity.HOUR);
      }
    } else if (upperGranularity == Calendar.HOUR) {
      if (start.length == 5 && end.length == 1) {
        int year = start[0] * 100 + start[1];
        int month = start[2];
        int day = start[3];
        int minute = start[4];
        int hour = end[0];
        return new TimeValue(year, month, day, hour, minute, TimeGranularity.MINUTE);
      }
    } else if (upperGranularity == Calendar.MINUTE) {
        if (start.length == 6 && end.length == 1) {
          int year = start[0] * 100 + start[1];
          int month = start[2];
          int day = start[3];
          int hour = start[4];
          int second = start[5];
          int minute = end[0];
          return new TimeValue(year, month, day, hour, minute, second);
        }
    }
    throw new IllegalArgumentException("Value length not match.");
  }

  @Override
  public ByteArrayRange[] decomposeRange(TimeData query) {
    if (!(query instanceof TimePeriod)) {
      throw new IllegalArgumentException("TimePeriodDimensionDefinition only support TimePeriod.");
    }
    TimeValue upperStartTime = query.getUpperStartTime();
    TimeValue upperEndTime = query.getUpperEndTime();
    TimeValue bottomStartTime = query.getBottomStartTime();
    TimeValue bottomEndTime = query.getBottomEndTime();

    TimeValue tmpUpperTime = upperStartTime;
    List<TimeValue> times = new ArrayList<>();
    while (tmpUpperTime.compareTo(upperEndTime) <= 0) {

      TimeValue tmpBottomTime = bottomStartTime;
      times.add(combineDateTime(tmpUpperTime, tmpBottomTime));
      while (tmpBottomTime.compareTo(bottomEndTime) < 0) {
        if (upperGranularity == Calendar.YEAR) {
          tmpBottomTime = tmpBottomTime.plusMonths(1);
        } else if (upperGranularity == Calendar.MONTH) {
          tmpBottomTime = tmpBottomTime.plusDays(1);
        } else if (upperGranularity == Calendar.DAY_OF_MONTH) {
          tmpBottomTime = tmpBottomTime.plusHours(1);
        } else if (upperGranularity == Calendar.HOUR) {
          tmpBottomTime = tmpBottomTime.plusMinutes(1);
        } else if (upperGranularity == Calendar.MINUTE) {
          tmpBottomTime = tmpBottomTime.plusSeconds(1);
        }
        if (tmpBottomTime.compareTo(bottomEndTime) < 0) {
          times.add(combineDateTime(tmpUpperTime, tmpBottomTime));
        }
      }

      if (upperGranularity == Calendar.YEAR) {
        tmpUpperTime = tmpUpperTime.plusYears(1);
      } else if (upperGranularity == Calendar.MONTH) {
        tmpUpperTime = tmpUpperTime.plusMonths(1);
      } else if (upperGranularity == Calendar. DAY_OF_MONTH) {
        tmpUpperTime = tmpUpperTime.plusDays(1);
      } else if (upperGranularity == Calendar.HOUR) {
        tmpUpperTime = tmpUpperTime.plusHours(1);
      } else if (upperGranularity == Calendar.MINUTE) {
        tmpUpperTime = tmpUpperTime.plusMinutes(1);
      }
    }

    ByteArrayRange[] ranges = new ByteArrayRange[times.size()];
    for (int i = 0; i < ranges.length; i++) {
      ranges[i] = normalize(times.get(i));
    }
    return ranges;
  }

  public HashMap<ByteArray, ByteArrayRange> getBinWithStartEnd(TimeData timeData) {
    ByteArrayRange[] ranges = decomposeRange(timeData);
    HashMap<ByteArray, ByteArrayRange> binStartEnd = new LinkedHashMap<>();
    for (ByteArrayRange range : ranges) {
      if (binStartEnd.containsKey(range.getStart())) {
        ByteArrayRange startEnd = binStartEnd.get(range.getStart());
        binStartEnd.put(range.getStart(), new ByteArrayRange(startEnd.getStart(), range.getEnd()));
      } else {
        binStartEnd.put(range.getStart(), new ByteArrayRange(range.getEnd(), range.getEnd()));
      }
    }
    return binStartEnd;
  }

  private TimeValue combineDateTime(TimeValue upperDateTime, TimeValue bottomDateTime) {
    if (upperGranularity == Calendar.YEAR) {
      return new TimeValue(upperDateTime.getYear(), bottomDateTime.getMonthValue(),
                           bottomDateTime.getDayOfMonth(), bottomDateTime.getHour(),
                           bottomDateTime.getMinute(), bottomDateTime.getSecond());
    } else if (upperGranularity == Calendar.MONTH) {
      return new TimeValue(upperDateTime.getYear(), upperDateTime.getMonthValue(),
                           bottomDateTime.getDayOfMonth(), bottomDateTime.getHour(),
                           bottomDateTime.getMinute(), bottomDateTime.getSecond());
    } else if (upperGranularity == Calendar.DAY_OF_MONTH) {
      return new TimeValue(upperDateTime.getYear(), upperDateTime.getMonthValue(),
                           upperDateTime.getDayOfMonth(), bottomDateTime.getHour(),
                           bottomDateTime.getMinute(), bottomDateTime.getSecond());
    } else if (upperGranularity == Calendar.HOUR) {
      return new TimeValue(upperDateTime.getYear(), upperDateTime.getMonthValue(),
                           upperDateTime.getDayOfMonth(), upperDateTime.getHour(),
                           bottomDateTime.getMinute(), bottomDateTime.getSecond());
    } else if (upperGranularity == Calendar.MINUTE) {
      return new TimeValue(upperDateTime.getYear(), upperDateTime.getMonthValue(),
                           upperDateTime.getDayOfMonth(), upperDateTime.getHour(),
                           upperDateTime.getMinute(), bottomDateTime.getSecond());
    }
    return null;
  }
}
