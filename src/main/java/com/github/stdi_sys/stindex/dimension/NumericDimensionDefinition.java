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
package com.github.stdi_sys.stindex.dimension;

import com.github.stdi_sys.stindex.dimension.bin.BinRange;
import com.github.stdi_sys.stindex.persist.Persistable;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericData;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericRange;

/**
 * The Numeric Dimension Definition interface defines the attributes and methods of a class which
 * forms the Space Filling Curve dimension.
 */
public interface NumericDimensionDefinition extends Persistable {
  double getRange();

  /**
   * Used to normalize a value within the bounds of the range to a percentage of the range between 0
   * and 1
   *
   * @return normalized value
   */
  double normalize(double value);

  /**
   * Used to denormalize the numeric data set from a value between 0 and 1 scaled to fit within its
   * native bounds
   *
   * @return the denormalized value
   */
  double denormalize(double value);

  /**
   * Returns the set of normalized ranges
   *
   * @param range a numeric range of the data set
   * @return an array of BinRange[] objects
   */
  BinRange[] getNormalizedRanges(NumericData range);

  /**
   * Returns a range in the native bounds of the dimension definition, denormalized from a bin and
   * separate range
   *
   * @param range a numeric range of the data set, with a bin
   * @return a NumericRange representing the given bin and range
   */
  NumericRange getDenormalizedRange(BinRange range);

  /**
   * If this numeric dimension definition uses bins, it is given a fixed length for the bin ID
   *
   * @return the fixed length for this dimensions bin ID
   */
  int getFixedBinIdSize();

  /**
   * Returns the native bounds of the dimension definition
   *
   * @return a range representing the minimum value and the maximum value for this dimension
   *         definition
   */
  NumericRange getBounds();

  /**
   * Provide the entire allowed range
   *
   * @return
   */
  NumericData getFullRange();
}
