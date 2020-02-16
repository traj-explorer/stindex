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
package com.github.stdi_sys.stindex.dimension.bin;

/** The Bin Value class is used to define the specific bins of a particular Binning Strategy. */
public class BinValue {
  private final byte[] binId;
  private final double normalizedValue;

  /**
   * Constructor used to create a new BinValue object based upon a normalized value
   *
   * @param normalizedValue the incoming value to be binned
   */
  public BinValue(final double normalizedValue) {
    this(null, normalizedValue);
  }

  /**
   * Constructor used to create a new BinValue object based upon a unique bin ID and normalized
   * value
   *
   * @param binId a unique ID to associate with this Bin Value
   * @param normalizedValue the incoming value to be binned
   */
  public BinValue(final byte[] binId, final double normalizedValue) {
    this.binId = binId;
    this.normalizedValue = normalizedValue;
  }

  /** @return a unique ID associated with this Bin Value */
  public byte[] getBinId() {
    return binId;
  }

  /** @return the normalized value of this particular Bin Value */
  public double getNormalizedValue() {
    return normalizedValue;
  }
}
