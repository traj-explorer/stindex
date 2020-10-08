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
public class TimePeriod implements TimeData {
  private TimeValue upperStartTime;
  private TimeValue upperEndTime;
  private TimeValue bottomStartTime;
  private TimeValue bottomEndTime;

  public TimePeriod(final TimeValue upperStartTime, final TimeValue upperEndTime,
                    final TimeValue bottomStartTime, final TimeValue bottomEndTime) {
    this.upperStartTime = upperStartTime;
    this.upperEndTime = upperEndTime;
    this.bottomStartTime = bottomStartTime;
    this.bottomEndTime = bottomEndTime;
  }

  @Override
  public TimeValue getStartTime() {
    return upperStartTime;
  }

  @Override
  public TimeValue getUpperStartTime() {
    return upperStartTime;
  }

  @Override
  public TimeValue getBottomStartTime() {
    return bottomStartTime;
  }

  @Override
  public TimeValue getEndTime() {
    return upperEndTime;
  }

  @Override
  public TimeValue getUpperEndTime() {
    return upperEndTime;
  }

  @Override
  public TimeValue getBottomEndTime() {
    return bottomEndTime;
  }

  @Override
  public boolean contains(TimeData other) {
    return false;
  }

  @Override
  public boolean intersects(TimeData other) {
    return false;
  }
}
