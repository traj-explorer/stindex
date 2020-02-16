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
 * Concrete implementation defining a single numeric value associated with a space filling curve.
 *
 * @author  Yu Liebing
 */
public class NumericValue implements NumericData {
  private double value;

  public NumericValue() {}

  /**
   * Constructor used to create a new NumericValue object
   *
   * @param value the particular numeric value
   */
  public NumericValue(final double value) {
    this.value = value;
  }

  /**
   * @return value the value of a numeric value object
   * */
  @Override
  public double getMin() {
    return value;
  }

  /**
   * @return value the value of a numeric value object
   * */
  @Override
  public double getMax() {
    return value;
  }

  /**
   * @return value the value of a numeric value object
   * */
  @Override
  public double getCentroid() {
    return value;
  }

  /**
   * Determines if this object is a range or not
   * */
  @Override
  public boolean isRange() {
    return false;
  }

  @Override
  public boolean contains(NumericData other) {
    // a numeric value cannot contains a numeric range
    if (other instanceof NumericRange) {
      return false;
    }
    return other.getCentroid() - value < EPSILON;
  }

  @Override
  public boolean intersects(NumericData other) {
    if (other instanceof NumericRange) {
      return value >= other.getMin() && value <= other.getMax();
    }
    return other.getCentroid() - value < EPSILON;
  }

  @Override
  public String toString() {
    return value + "";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(value);
    result = (prime * result) + (int) (temp ^ (temp >>> 32));
    return result;
  }

  protected static final double EPSILON = 1E-10;

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
    final NumericValue other = (NumericValue) obj;
    return (Math.abs(value - other.value) < EPSILON);
  }

  @Override
  public byte[] toBinary() {
    final ByteBuffer buf = ByteBuffer.allocate(8);
    buf.putDouble(value);
    return buf.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    value = buf.getDouble();
  }
}
