package com.github.tm.stindex.dimension;

import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.temporal.data.TimeData;
import com.github.tm.stindex.temporal.data.TimeValue;

/**
 * @author Yu Liebing
 */
public interface TimeDimensionDefinition {
  ByteArrayRange normalize(TimeValue time);

  TimeValue denormalize(ByteArrayRange value);

  ByteArrayRange[] decomposeRange(TimeData query);
}
