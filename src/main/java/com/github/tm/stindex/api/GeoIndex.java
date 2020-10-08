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
package com.github.tm.stindex.api;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.dimension.BasicDimensionDefinition;
import com.github.tm.stindex.spatial.h3.H3Index;
import com.github.tm.stindex.spatial.sfc.RangeDecomposition;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.GridIndex;
import com.github.tm.stindex.spatial.sfc.SFCFactory;
import com.github.tm.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;

import java.nio.ByteBuffer;

/**
 * @author Yu Liebing
 */
public class GeoIndex {

  private GridIndex gridIndex;
  private int res;
  private SpatialIndexType type;

  public GeoIndex(SpatialIndexType type, int res) {
    this.res = res;
    this.type = type;
    final SFCDimensionDefinition[] dimensions = {
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), res),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), res)};
    switch (type) {
      case ZOrder:
        gridIndex = SFCFactory.createSpaceFillingCurve(dimensions, SFCFactory.SFCType.ZORDER);
        break;
      case Hilbert:
        gridIndex = SFCFactory.createSpaceFillingCurve(dimensions, SFCFactory.SFCType.HILBERT);
        break;
      case XZOrder:
        gridIndex = SFCFactory.createSpaceFillingCurve(dimensions, SFCFactory.SFCType.XZORDER);
        break;
      case H3:
        gridIndex = new H3Index(res);
        break;
    }
  }

  public ByteArray getIndex(double lon, double lat) {
    return gridIndex.getIndex(new double[] {lon, lat});
  }

  public ByteArrayRange[] decomposeRange(
          double minLon, double maxLon,
          double minLat, double maxLat) {
    NumericRange lonRange = new NumericRange(minLon, maxLon);
    NumericRange latRange = new NumericRange(minLat, maxLat);
    BasicNumericDataset dataset = new BasicNumericDataset(new NumericData[] {lonRange, latRange});
    RangeDecomposition rangeDecomposition = gridIndex.decomposeRangeFully(dataset);
    return rangeDecomposition.getRanges();
  }

  public ByteArrayRange getFatherRange(ByteArrayRange childrenRange) {
    return gridIndex.getFatherRange(childrenRange);
  }

  public ByteArrayRange[] getRing(ByteArrayRange centerRange) {
    return gridIndex.getRing(centerRange);
  }

  public BoundingBox getSpatialBoundingBox(ByteArray index) {
    MultiDimensionalNumericData data = gridIndex.getRanges(index);
    NumericData[] numericData = data.getDataPerDimension();
    NumericData lonRange = numericData[0];
    NumericData latRange = numericData[1];
    return new BoundingBox(latRange.getMin(), latRange.getMax(), lonRange.getMin(), lonRange.getMax());
  }

  public int getRes() {
    return res;
  }

  public SpatialIndexType getType() {
    return type;
  }

  public byte[] toBytes() {
    ByteBuffer buf = ByteBuffer.allocate(5);
    buf.putInt(res);
    buf.put(type.getValue());
    return buf.array();
  }

  public static GeoIndex fromBytes(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes);
    int res = buf.getInt();
    byte type = buf.get();
    return new GeoIndex(SpatialIndexType.valueOf(type), res);
  }
}
