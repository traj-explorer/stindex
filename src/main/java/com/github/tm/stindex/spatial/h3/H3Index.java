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
package com.github.tm.stindex.spatial.h3;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.SpatialByteArray;
import com.github.tm.stindex.spatial.GridIndex;
import com.github.tm.stindex.spatial.sfc.RangeDecomposition;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.uber.h3core.H3Core;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Yu Liebing
 */
public class H3Index implements GridIndex {
  private H3Core h3Core;
  private int res;

  public H3Index(int res) {
    try {
      h3Core = H3Core.newInstance();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.res = res;
  }

  @Override
  public ByteArray getIndex(double[] values) {
    double lon = values[0];
    double lat = values[1];
    long index = h3Core.geoToH3(lat, lon, res);
    return new SpatialByteArray(index, res);
  }

  @Override
  public MultiDimensionalNumericData getRanges(ByteArray index) {
    return null;
  }

  @Override
  public MultiDimensionalNumericData getRanges(ByteArrayRange indexRange) {
    return null;
  }

  @Override
  public long[] getCoordinates(final ByteArray index) {
    return new long[0];
  }

  @Override
  public RangeDecomposition decomposeRangeFully(MultiDimensionalNumericData query) {
    return null;
  }

  @Override
  public RangeDecomposition decomposeRange(MultiDimensionalNumericData query, int maxRanges) {
    return null;
  }

  @Override
  public BigInteger getEstimatedIdCount(MultiDimensionalNumericData data) {
    return null;
  }

  @Override
  public long[] normalizeRange(double minValue, double maxValue, int dimension) {
    return new long[0];
  }

  @Override
  public double[] getInsertionIdRangePerDimension() {
    return new double[0];
  }

  @Override
  public ByteArrayRange getFatherRange(ByteArrayRange childRange) {
    return null;
  }

  @Override
  public ByteArrayRange[] getRing(ByteArrayRange center) {

    return new ByteArrayRange[0];
  }

  @Override
  public byte[] toBinary() {
    return new byte[0];
  }

  @Override
  public void fromBinary(byte[] bytes) {

  }
}
