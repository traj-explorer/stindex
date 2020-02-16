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
package com.github.stdi_sys.stindex.spatial.sfc.data;

import java.nio.ByteBuffer;

/**
 * Concrete implementation defining a numeric range associated with a space filling curve.
 *
 * @author Yu Liebing
 * */
public class NumericRange implements NumericData {
  private double min;
  private double max;

  public NumericRange() {}

  /**
   * Constructor used to create a IndexRange object
   *
   * @param min the minimum bounds of a unique index range
   * @param max the maximum bounds of a unique index range
   */
  public NumericRange(final double min, final double max) {
    this.min = min;
    this.max = max;
  }

  /**
   * @return min the minimum bounds of a index range object
   * */
  @Override
  public double getMin() {
    return min;
  }

  /**
   * @return max the maximum bounds of a index range object
   * */
  @Override
  public double getMax() {
    return max;
  }

  /**
   * @return centroid the center of a unique index range object
   * */
  @Override
  public double getCentroid() {
    return (min + max) / 2;
  }

  /**
   * Flag to determine if the object is a range
   * */
  @Override
  public boolean isRange() {
    return true;
  }

  @Override
  public boolean contains(NumericData other) {
    return other.getMin() >= min && other.getMax() <= max;
  }

  @Override
  public boolean intersects(NumericData other) {
    return !((other.getMax() < min) || (other.getMin() > max));
  }

  @Override
  public String toString() {
    return "[" + min + ", " + max + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(max);
    result = (prime * result) + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(min);
    result = (prime * result) + (int) (temp ^ (temp >>> 32));
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
    // changing this check will fail some unit tests.
    if (!NumericRange.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final NumericRange other = (NumericRange) obj;
    return (Math.abs(max - other.max) < NumericValue.EPSILON)
        && (Math.abs(min - other.min) < NumericValue.EPSILON);
  }

  @Override
  public byte[] toBinary() {
    final ByteBuffer buf = ByteBuffer.allocate(16);
    buf.putDouble(min);
    buf.putDouble(max);
    return buf.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    min = buf.getDouble();
    max = buf.getDouble();
  }
}
