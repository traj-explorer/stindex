package com.github.tm.stindex.st;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.github.tm.stindex.temporal.data.TimeData;
import com.github.tm.stindex.temporal.data.TimeValue;

/**
 * @author Yu Liebing
 */
public interface STEncoding {
  ByteArray getIndex(TimeValue time, double[] values);

  ByteArrayRange[] decomposeRanges(TimeData queryTime, MultiDimensionalNumericData querySpatial);

  ByteArrayRange getSpatialFather(ByteArrayRange index);

  ByteArrayRange[] getSpatialRing(ByteArrayRange index);
}
