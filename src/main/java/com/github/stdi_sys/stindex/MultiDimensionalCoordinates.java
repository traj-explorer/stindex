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

public class MultiDimensionalCoordinates {
  // this is a generic placeholder for tiers
  private final byte[] multiDimensionalId;
  private final Coordinate[] coordinatePerDimension;

  public MultiDimensionalCoordinates() {
    multiDimensionalId = new byte[] {};
    coordinatePerDimension = new Coordinate[] {};
  }

  public MultiDimensionalCoordinates(
      final byte[] multiDimensionalId,
      final Coordinate[] coordinatePerDimension) {
    super();
    this.multiDimensionalId = multiDimensionalId;
    this.coordinatePerDimension = coordinatePerDimension;
  }

  public byte[] getMultiDimensionalId() {
    return multiDimensionalId;
  }

  public Coordinate getCoordinate(final int dimension) {
    return coordinatePerDimension[dimension];
  }

  public int getNumDimensions() {
    return coordinatePerDimension.length;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(coordinatePerDimension);
    result = (prime * result) + Arrays.hashCode(multiDimensionalId);
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
    final MultiDimensionalCoordinates other = (MultiDimensionalCoordinates) obj;
    if (!Arrays.equals(coordinatePerDimension, other.coordinatePerDimension)) {
      return false;
    }
    if (!Arrays.equals(multiDimensionalId, other.multiDimensionalId)) {
      return false;
    }
    return true;
  }
}
