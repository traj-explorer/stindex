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
package com.github.tm.stindex.spatial;

import java.math.BigInteger;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.persist.Persistable;
import com.github.tm.stindex.spatial.sfc.RangeDecomposition;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;

/**
 * Base class which defines common methods for any discrete global grid systems (DGGSs). Hosts
 * standard access methods shared between implementation. A DGGS is expected to provide a reversible
 * n-dimensional -> 1-dimensional mapping.
 *
 * @author Yu Liebing
 */
public interface GridIndex extends Persistable {
  /**
   * Maps a n-dimensional value to a single dimension, i.e. [12,33] -> 0033423
   *
   * @param values n-dimensional value to be encoded in the DGGS. The size of value corresponds
   *               to the number of dimensions
   * @return value derived from the the DGGS transform.
   */
  ByteArray getIndex(double[] values);

  /**
   * Gets n-dimensional ranges from a single dimension, i.e. 0033423 -> [12,33]
   *
   * @param index the DGGS index to calculate the ranges of values represented.
   * @return the valid ranges per dimension of a single DGGS index derived from the the DGGS transform.
   */
  MultiDimensionalNumericData getRanges(ByteArray index);

  /**
   * Gets n-dimensional ranges from a single dimension range, i.e. [0033423, 0033435] -> [12, 66]
   *
   * @param indexRange the DGGS index range to calculate the ranges of values represented.
   * @return the valid ranges per dimension of a DGGS index range derived from the the DGGS transform.
   */
  MultiDimensionalNumericData getRanges(ByteArrayRange indexRange);

  /**
   * Gets n-dimensional coordinates from a single dimension
   *
   * @param index the DGGS index to calculate the coordinates for each dimension.
   * @return the coordinate in each dimension for the given index
   */
  long[] getCoordinates(ByteArray index);

  /**
   * Returns a collection of ranges on the 1-d space filling curve that correspond to the
   * n-dimensional range described in the query parameter.
   *
   * <p> This method will decompose the range all the way down to the unit interval of 1.
   *
   * @param query describes the n-dimensional query window that will be decomposed
   * @return an object containing the ranges on the DGGS that overlap the parameters supplied
   *         in the query object
   */
  RangeDecomposition decomposeRangeFully(MultiDimensionalNumericData query);

  /**
   * Returns a collection of ranges on the 1-d space filling curve that correspond to the
   * n-dimensional range described in the query parameter.
   *
   * <p> This method will roll up the ranges based on the maxRanges parameter. Ranges will be
   * "connected" based on the minimization of distance between the end of one range and the start of
   * the next.
   *
   * @param query describes the n-dimensional query window that will be decomposed
   * @return an object containing the ranges on the DGGS that overlap the parameters supplied in the
   *         query object
   */
  RangeDecomposition decomposeRange(
          MultiDimensionalNumericData query,
          int maxRanges);

  /**
   * Determines the estimated number of rows a multi-dimensional range will span within this DGGS
   *
   * @param data describes the n-dimensional range to estimate the row count for
   * @return an estimate of the row count for the ranges given within this DGGS
   */
  BigInteger getEstimatedIdCount(MultiDimensionalNumericData data);

  /**
   * Determines the coordinates within this DGGS for a dimension given a range
   *
   * @param minValue describes the minimum of a range in a single dimension used to determine the
   *        DGGS coordinate range
   * @param maxValue describes the maximum of a range in a single dimension used to determine the
   *        DGGS coordinate range
   * @param dimension the dimension
   * @return the range of coordinates as an array where the first element is the min and the second
   *         element is the max
   */
  long[] normalizeRange(double minValue, double maxValue, int dimension);

  /**
   * Get the range/size of a single insertion ID for each dimension
   *
   * @return the range of a single insertion ID for each dimension
   */
  double[] getInsertionIdRangePerDimension();

  /**
   * Get the father spatial cell's index range for a given spatial cell's range.
   * E.g. Below is a hilbert curve with resolution 2. This method will return [0, 15] for [0, 2].
   * -------------
   * |05|06|09|10|
   * -------------
   * |04|07|08|11|
   * -------------
   * |03|02|13|12|
   * -------------
   * |00|01|14|15|
   * -------------
   * @param childRange given spatial cell(s)'(s) index range to get its father spatial cells' index range
   * @return father spatial cells' index range
   * */
  ByteArrayRange getFatherRange(ByteArrayRange childRange);

  /**
   *
   * */
  ByteArrayRange[] getRing(ByteArrayRange center);
}
