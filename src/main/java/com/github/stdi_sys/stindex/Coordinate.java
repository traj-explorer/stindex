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
package com.github.stdi_sys.stindex;

import java.util.Arrays;

public class Coordinate {
  private long coordinate;
  private byte[] binId;

  protected Coordinate() {}

  public Coordinate(final long coordinate, final byte[] binId) {
    this.coordinate = coordinate;
    this.binId = binId;
  }

  public long getCoordinate() {
    return coordinate;
  }

  public void setCoordinate(final long coordinate) {
    this.coordinate = coordinate;
  }

  public byte[] getBinId() {
    return binId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(binId);
    result = (prime * result) + (int) (coordinate ^ (coordinate >>> 32));
    result = (prime * result) + Arrays.hashCode(binId);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Coordinate other = (Coordinate) obj;
    if (!Arrays.equals(binId, other.binId)) {
      return false;
    }
    if (coordinate != other.coordinate) {
      return false;
    }
    return true;
  }
}
