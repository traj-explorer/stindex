package com.github.stdi_sys.stindex.st;

import com.github.stdi_sys.stindex.ByteArray;
import com.github.stdi_sys.stindex.ByteArrayRange;
import com.github.stdi_sys.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.github.stdi_sys.stindex.temporal.data.TimeData;
import com.github.stdi_sys.stindex.temporal.data.TimeValue;

/**
 * @author Yu Liebing
 */
public interface STEncoding {
  ByteArray getIndex(TimeValue time, double[] values);

  ByteArrayRange[] decomposeRanges(TimeData queryTime, MultiDimensionalNumericData querySpatial);

  ByteArrayRange getSpatialFather(ByteArrayRange index);

  ByteArrayRange[] getSpatialRing(ByteArrayRange index);
}
