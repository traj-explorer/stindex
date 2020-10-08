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
package com.github.tm.stindex;

import com.github.tm.stindex.dimension.NumericDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;

/** Interface which defines a numeric index strategy. */
public interface NumericIndexStrategy extends
    SortedIndexStrategy<MultiDimensionalNumericData, MultiDimensionalNumericData>,
    PartitionIndexStrategy<MultiDimensionalNumericData, MultiDimensionalNumericData> {

  /**
   * Return an integer coordinate in each dimension for the given insertion ID plus a bin ID if that
   * dimension is continuous
   *
   * @param insertionId the insertion ID to determine the coordinates for
   * @return the integer coordinate that the given insertion ID represents and associated bin ID if
   *         that dimension is continuous
   */
  public MultiDimensionalCoordinates getCoordinatesPerDimension(
          byte[] partitionKey,
          byte[] sortKey);

  /**
   * Return an integer coordinate range in each dimension for the given data range plus a bin ID if
   * that dimension is continuous
   *
   * @param dataRange the range to determine the coordinates for
   * @return the integer coordinate ranges that the given data ID represents and associated bin IDs
   *         if a dimension is continuous
   */
  public MultiDimensionalCoordinateRanges[] getCoordinateRangesPerDimension(
          MultiDimensionalNumericData dataRange,
          IndexMetaData... hints);

  /**
   * Returns an array of dimension definitions that defines this index strategy, the array is in the
   * order that is expected within multidimensional numeric data that is passed to this index
   * strategy
   *
   * @return the ordered array of dimension definitions that represents this index strategy
   */
  public NumericDimensionDefinition[] getOrderedDimensionDefinitions();

  /**
   * * Get the range/size of a single insertion ID for each dimension at the highest precision
   * supported by this index strategy
   *
   * @return the range of a single insertion ID for each dimension
   */
  public double[] getHighestPrecisionIdRangePerDimension();

  /**
   * * Get the offset in bytes before the dimensional index. This can accounts for tier IDs and bin
   * IDs
   *
   * @return the byte offset prior to the dimensional index
   */
  @Override
  public int getPartitionKeyLength();
}
