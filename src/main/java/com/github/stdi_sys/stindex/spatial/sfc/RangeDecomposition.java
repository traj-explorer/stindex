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
package com.github.stdi_sys.stindex.spatial.sfc;

import com.github.stdi_sys.stindex.ByteArrayRange;

/** * This class encapsulates a set of ranges returned from a space filling curve decomposition. */
public class RangeDecomposition {
  private final ByteArrayRange[] ranges;

  /**
   * Constructor used to create a new Range Decomposition object.
   *
   * @param ranges ranges for the space filling curve
   */
  public RangeDecomposition(final ByteArrayRange[] ranges) {
    this.ranges = ranges;
  }

  /** @return the ranges associated with this Range Decomposition */
  public ByteArrayRange[] getRanges() {
    return ranges;
  }
}
