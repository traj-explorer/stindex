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
import com.github.stdi_sys.stindex.ByteArrayRange;

/**
 * @author Yu Liebing
 */
public class SpatialCell {
  private ByteArrayRange spatialIndex;
  private int res;
  private SpatialIndexType type;

  public SpatialCell(ByteArray spatialIndex, int res, SpatialIndexType type) {
    this.spatialIndex = new ByteArrayRange(spatialIndex, spatialIndex, true);
    this.res = res;
    this.type = type;
  }

  public SpatialCell(ByteArrayRange spatialIndex, int res, SpatialIndexType type) {
    this.spatialIndex = spatialIndex;
    this.res = res;
    this.type = type;
  }

  public ByteArrayRange getSpatialIndex() {
    return spatialIndex;
  }

  public int getRes() {
    return res;
  }

  public SpatialIndexType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "GeoIndex: " + spatialIndex + ", Resolution: " + res + ", IndexType: " + type;
  }
}
