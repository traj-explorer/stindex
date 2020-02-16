package com.github.stdi_sys.stindex.dimension;

import com.github.stdi_sys.stindex.ByteArrayRange;
import com.github.stdi_sys.stindex.temporal.data.TimeData;
import com.github.stdi_sys.stindex.temporal.data.TimeValue;

/**
 * @author Yu Liebing
 */
public interface TimeDimensionDefinition {
  ByteArrayRange normalize(TimeValue time);

  TimeValue denormalize(ByteArrayRange value);

  ByteArrayRange[] decomposeRange(TimeData query);
}
