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
package com.github.stdi_sys.stindex.temporal.data;

import java.time.LocalDateTime;

/**
 * This class is used to represent time values with upper and bottom granularity.
 * Currently supported time granularity includes year, month, day, hour, minute and second.
 * Note that this class only considers date-based time, and its time zone information is
 * not self-contained.
 *
 * <p>The time format provided is as follows:
 * yyyy             MM          dd        HH      mm    ss
 * yyyyMM           MMdd        ddHH      HHmm    mmss
 * yyyyMMdd         MMddHH      ddHHmm    HHmmss
 * yyyyMMddHH       MMddHHmm    ddHHmmss
 * yyyyMMddHHmm     MMddHHmmss
 * yyyyMMddHHmmss
 *
 * @author Yu Liebing
 */
public class TimeValue implements Comparable<TimeValue> {
  public static final int DEFAULT_YEAR = 1970;
  public static final int DEFAULT_MONTH = 1;
  public static final int DEFAULT_DAY = 1;
  public static final int DEFAULT_HOUR = 0;
  public static final int DEFAULT_MINUTE = 0;
  public static final int DEFAULT_SECOND = 0;

  private TimeGranularity upperGranularity;
  private TimeGranularity bottomGranularity;
  // default time
  private int year = DEFAULT_YEAR;
  private int month = DEFAULT_MONTH;
  private int day = DEFAULT_DAY;
  private int hour = DEFAULT_HOUR;
  private int minute = DEFAULT_MINUTE;
  private int second = DEFAULT_SECOND;

  public TimeValue() {}

  public TimeValue(final int v1, final TimeGranularity bottomGranularity) {
    this.bottomGranularity = bottomGranularity;
    switch (bottomGranularity) {
      case YEAR:
        year = v1;
        upperGranularity = TimeGranularity.YEAR;
        break;
      case MONTH:
        month = v1;
        upperGranularity = TimeGranularity.MONTH;
        break;
      case DAY:
        day = v1;
        upperGranularity = TimeGranularity.DAY;
        break;
      case HOUR:
        hour = v1;
        upperGranularity = TimeGranularity.HOUR;
        break;
      case MINUTE:
        minute = v1;
        upperGranularity = TimeGranularity.MINUTE;
        break;
      case SECOND:
        second = v1;
        upperGranularity = TimeGranularity.SECOND;
        break;
    }
  }

  public TimeValue(final int v1, final int v2, final TimeGranularity bottomGranularity) {
    if (bottomGranularity.getValue() < TimeGranularity.MONTH.getValue()) {
      throw new IllegalArgumentException("Unsupported granularity.");
    }
    this.bottomGranularity = bottomGranularity;
    switch (bottomGranularity) {
      case MONTH:
        year = v1;
        month = v2;
        upperGranularity = TimeGranularity.YEAR;
        break;
      case DAY:
        month = v1;
        day = v2;
        upperGranularity = TimeGranularity.MONTH;
        break;
      case HOUR:
        day = v1;
        hour = v2;
        upperGranularity = TimeGranularity.DAY;
        break;
      case MINUTE:
        hour = v1;
        minute = v2;
        upperGranularity = TimeGranularity.HOUR;
        break;
      case SECOND:
        minute = v1;
        second = v2;
        upperGranularity = TimeGranularity.MINUTE;
        break;
    }
  }

  public TimeValue(int v1, int v2, int v3, TimeGranularity bottomGranularity) {
    if (bottomGranularity.getValue() < TimeGranularity.DAY.getValue()) {
      throw new IllegalArgumentException("Unsupported granularity.");
    }
    this.bottomGranularity = bottomGranularity;
    switch (bottomGranularity) {
      case DAY:
        year = v1;
        month = v2;
        day = v3;
        upperGranularity = TimeGranularity.YEAR;
        break;
      case HOUR:
        month = v1;
        day = v2;
        hour = v3;
        upperGranularity = TimeGranularity.MONTH;
        break;
      case MINUTE:
        day = v1;
        hour = v2;
        minute = v3;
        upperGranularity = TimeGranularity.DAY;
        break;
      case SECOND:
        hour = v1;
        minute = v2;
        second = v3;
        upperGranularity = TimeGranularity.HOUR;
        break;
    }
  }

  public TimeValue(int v1, int v2, int v3, int v4, TimeGranularity bottomGranularity) {
    if (bottomGranularity.getValue() < TimeGranularity.HOUR.getValue()) {
      throw new IllegalArgumentException("Unsupported granularity.");
    }
    this.bottomGranularity = bottomGranularity;
    switch (bottomGranularity) {
      case HOUR:
        year = v1;
        month = v2;
        day = v3;
        hour = v4;
        upperGranularity = TimeGranularity.YEAR;
        break;
      case MINUTE:
        month = v1;
        day = v2;
        hour = v3;
        minute = v4;
        upperGranularity = TimeGranularity.MONTH;
        break;
      case SECOND:
        day = v1;
        hour = v2;
        minute = v3;
        second = v4;
        upperGranularity = TimeGranularity.DAY;
        break;
    }
  }

  public TimeValue(int v1, int v2, int v3, int v4, int v5, TimeGranularity bottomGranularity) {
    if (bottomGranularity.getValue() < TimeGranularity.MINUTE.getValue()) {
      throw new IllegalArgumentException("Unsupported granularity.");
    }
    this.bottomGranularity = bottomGranularity;
    switch (bottomGranularity) {
      case MINUTE:
        year = v1;
        month = v2;
        day = v3;
        hour = v4;
        minute = v5;
        upperGranularity = TimeGranularity.YEAR;
        break;
      case SECOND:
        month = v1;
        day = v2;
        hour = v3;
        minute = v4;
        second = v5;
        upperGranularity = TimeGranularity.MONTH;
        break;
    }
  }

  public TimeValue(int year, int month, int day, int hour, int minute, int second) {
    this.upperGranularity = TimeGranularity.YEAR;
    this.bottomGranularity = TimeGranularity.SECOND;
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
  }

  public TimeValue plusYears(long addYear) {
    LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
    dateTime = dateTime.plusYears(addYear);
    return new TimeValue(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                         dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
  }

  public TimeValue plusMonths(long addMonth) {
    LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
    dateTime = dateTime.plusMonths(addMonth);
    return new TimeValue(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                         dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
  }

  public TimeValue plusDays(long addDays) {
    LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
    dateTime = dateTime.plusDays(addDays);
    return new TimeValue(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                         dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
  }

  public TimeValue plusHours(long addHours) {
    LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
    dateTime = dateTime.plusHours(addHours);
    return new TimeValue(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                         dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
  }

  public TimeValue plusMinutes(long addMinutes) {
    LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
    dateTime = dateTime.plusMinutes(addMinutes);
    return new TimeValue(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                         dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
  }

  public TimeValue plusSeconds(long addSeconds) {
    LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
    dateTime = dateTime.plusMinutes(addSeconds);
    return new TimeValue(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                         dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
  }

  @Override
  public String toString() {
    String format = "";
    if (upperGranularity.getValue() == TimeGranularity.YEAR.getValue()) {
      if (bottomGranularity.getValue() == TimeGranularity.YEAR.getValue()) {
        format = String.format("%04d-XX-XXTXX:XX:XX", year);
      } else if (bottomGranularity.getValue() == TimeGranularity.MONTH.getValue()) {
        format = String.format("%04d-%02d-XXTXX:XX:XX", year, month);
      } else if (bottomGranularity.getValue() == TimeGranularity.DAY.getValue()) {
        format = String.format("%04d-%02d-%02dTXX:XX:XX", year, month, day);
      } else if (bottomGranularity.getValue() == TimeGranularity.HOUR.getValue()) {
        format = String.format("%04d-%02d-%02dT%02d:XX:XX", year, month, day, hour);
      } else if (bottomGranularity.getValue() == TimeGranularity.MINUTE.getValue()) {
        format = String.format("%04d-%02d-%02dT%02d:%02d:XX", year, month, day, hour, minute);
      } else if (bottomGranularity.getValue() == TimeGranularity.SECOND.getValue()) {
        format = String.format("%04d-%02d-%02dT%02d:%02d:%02d", year, month, day, hour, minute, second);
      }
    } else if (upperGranularity.getValue() == TimeGranularity.MONTH.getValue()) {
      if (bottomGranularity.getValue() == TimeGranularity.MONTH.getValue()) {
        format = String.format("XXXX-%02d-XXTXX:XX:XX", month);
      } else if (bottomGranularity.getValue() == TimeGranularity.DAY.getValue()) {
        format = String.format("XXXX-%02d-%02dTXX:XX:XX", month, day);
      } else if (bottomGranularity.getValue() == TimeGranularity.HOUR.getValue()) {
        format = String.format("XXXX-%02d-%02dT%02d:XX:XX", month, day, hour);
      } else if (bottomGranularity.getValue() == TimeGranularity.MINUTE.getValue()) {
        format = String.format("XXXX-%02d-%02dT%02d:%02d:XX", month, day, hour, minute);
      } else if (bottomGranularity.getValue() == TimeGranularity.SECOND.getValue()) {
        format = String.format("XXXX-%02d-%02dT%02d:%02d:%02d", month, day, hour, minute, second);
      }
    } else if (upperGranularity.getValue() == TimeGranularity.DAY.getValue()) {
      if (bottomGranularity.getValue() == TimeGranularity.DAY.getValue()) {
        format = String.format("XXXX-XX-%02dTXX:XX:XX", day);
      } else if (bottomGranularity.getValue() == TimeGranularity.HOUR.getValue()) {
        format = String.format("XXXX-XX-%02dT%02d:XX:XX", day, hour);
      } else if (bottomGranularity.getValue() == TimeGranularity.MINUTE.getValue()) {
        format = String.format("XXXX-XX-%02dT%02d:%02d:XX", day, hour, minute);
      } else if (bottomGranularity.getValue() == TimeGranularity.SECOND.getValue()) {
        format = String.format("XXXX-XX-%02dT%02d:%02d:%02d", day, hour, minute, second);
      }
    } else if (upperGranularity.getValue() == TimeGranularity.HOUR.getValue()) {
      if (bottomGranularity.getValue() == TimeGranularity.HOUR.getValue()) {
        format = String.format("XXXX-XX-XXT%02d:XX:XX", hour);
      } else if (bottomGranularity.getValue() == TimeGranularity.MINUTE.getValue()) {
        format = String.format("XXXX-XX-XXT%02d:%02d:XX", hour, minute);
      } else if (bottomGranularity.getValue() == TimeGranularity.SECOND.getValue()) {
        format = String.format("XXXX-XX-XXT%02d:%02d:%02d", hour, minute, second);
      }
    } else if (upperGranularity.getValue() == TimeGranularity.MINUTE.getValue()) {
      if (bottomGranularity.getValue() == TimeGranularity.MINUTE.getValue()) {
        format = String.format("XXXX-XX-XXTXX:%02d:XX", minute);
      } else if (bottomGranularity.getValue() == TimeGranularity.SECOND.getValue()) {
        format = String.format("XXXX-XX-XXTXX:%02d:%02d", minute, second);
      }
    } else if (upperGranularity.getValue() == TimeGranularity.SECOND.getValue()) {
      if (bottomGranularity.getValue() == TimeGranularity.SECOND.getValue()) {
        format = String.format("XXXX-XX-XXTXX:XX:%02d", second);
      }
    }
    return format;
  }

  public TimeGranularity getUpperGranularity() {
    return upperGranularity;
  }

  public TimeGranularity getBottomGranularity() {
    return bottomGranularity;
  }

  public int getYear() {
    return year;
  }

  public int getMonthValue() {
    return month;
  }

  public int getDayOfMonth() {
    return day;
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  public int getSecond() {
    return second;
  }

  @Override
  public int compareTo(TimeValue o) {
    LocalDateTime dateTime1 = LocalDateTime.of(year, month, day, hour, minute, second);
    LocalDateTime dateTime2 = LocalDateTime.of(o.year, o.month, o.day, o.hour, o.minute, o.second);
    return dateTime1.compareTo(dateTime2);
  }

  public LocalDateTime toLocalDateTime() {
    return LocalDateTime.of(year, month, day, hour, minute, second);
  }
}
