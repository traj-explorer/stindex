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
package com.github.stdi_sys.stindex.api;

import com.github.stdi_sys.stindex.ByteArray;
import com.github.stdi_sys.stindex.dimension.BasicDimensionDefinition;
import com.github.stdi_sys.stindex.spatial.h3.H3Index;
import com.github.stdi_sys.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.stdi_sys.stindex.spatial.GridIndex;
import com.github.stdi_sys.stindex.spatial.sfc.SFCFactory;

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

  public SpatialCell getSpatialCell(double lon, double lat) {
    ByteArray index = gridIndex.getIndex(new double[] {lon, lat});
    return new SpatialCell(index, res, type);
  }

}
