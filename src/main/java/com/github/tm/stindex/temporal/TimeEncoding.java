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

import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.dimension.TimeDimensionDefinition;
import com.github.tm.stindex.temporal.data.TimeData;
import com.github.tm.stindex.temporal.data.TimeValue;

/**
 * @author Yu Liebing
 */
public interface TimeEncoding {
  /**
   * Get the binary representation of current temporal encoding method.
   *
   * @param time time to be index
   * @return index time
   * */
  ByteArrayRange getIndex(TimeValue time);

  TimeValue getTemporalRange(ByteArrayRange range);

  ByteArrayRange[] decomposeRange(TimeData query);

  TimeDimensionDefinition getTimeDimensionDefinition();
}
