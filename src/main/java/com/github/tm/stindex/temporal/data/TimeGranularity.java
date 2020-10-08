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

import java.util.Calendar;

/**
 * @author Yu Liebing
 */
public enum TimeGranularity {
  YEAR(Calendar.YEAR),
  MONTH(Calendar.MONTH),
  DAY(Calendar.DAY_OF_MONTH),
  HOUR(Calendar.HOUR),
  MINUTE(Calendar.MINUTE),
  SECOND(Calendar.SECOND);

  private int value;

  TimeGranularity(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
