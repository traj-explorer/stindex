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
package com.github.tm.stindex.temporal.data;

/**
 * @author Yu Liebing
 */
public class TimeRange implements TimeData {
  private TimeValue startTime;
  private TimeValue endTime;

  public TimeRange(final TimeValue startTime, final TimeValue endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  @Override
  public TimeValue getStartTime() {
    return startTime;
  }

  @Override
  public TimeValue getUpperStartTime() {
    return startTime;
  }

  @Override
  public TimeValue getBottomStartTime() {
    return startTime;
  }

  @Override
  public TimeValue getEndTime() {
    return endTime;
  }

  @Override
  public TimeValue getUpperEndTime() {
    return endTime;
  }

  @Override
  public TimeValue getBottomEndTime() {
    return endTime;
  }

  @Override
  public boolean contains(TimeData other) {
    return false;
  }

  @Override
  public boolean intersects(TimeData other) {
    return false;
  }

  @Override
  public String toString() {
    return "[" + startTime + ", " + endTime + "]";
  }
}
