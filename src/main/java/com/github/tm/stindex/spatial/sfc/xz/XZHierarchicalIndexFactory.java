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
package com.github.tm.stindex.spatial.sfc.xz;

import com.github.tm.stindex.dimension.NumericDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.SFCFactory;
import com.github.tm.stindex.spatial.sfc.tiered.TieredSFCIndexFactory;
import com.github.tm.stindex.spatial.sfc.tiered.TieredSFCIndexStrategy;

/**
 * A factory for creating a Hierachical XZ Index strategy with a TieredSFCIndexStrategy substrategy
 * using various approaches for breaking down the bits of precision per tier
 */
public class XZHierarchicalIndexFactory {

  public static XZHierarchicalIndexStrategy createFullIncrementalTieredStrategy(
      final NumericDimensionDefinition[] baseDefinitions,
      final int[] maxBitsPerDimension,
      final SFCFactory.SFCType sfcType) {
    return createFullIncrementalTieredStrategy(baseDefinitions, maxBitsPerDimension, sfcType, null);
  }

  /**
   * @param baseDefinitions an array of Numeric Dimension Definitions
   * @param maxBitsPerDimension the max cardinality for the Index Strategy
   * @param sfcType the type of space filling curve (e.g. Hilbert)
   * @param maxEstimatedDuplicatedIds the max number of duplicate SFC IDs
   * @return an Index Strategy object with a tier for every incremental cardinality between the
   *         lowest max bits of precision and 0
   */
  public static XZHierarchicalIndexStrategy createFullIncrementalTieredStrategy(
      final NumericDimensionDefinition[] baseDefinitions,
      final int[] maxBitsPerDimension,
      final SFCFactory.SFCType sfcType,
      final Long maxEstimatedDuplicatedIds) {
    final TieredSFCIndexStrategy rasterStrategy =
        TieredSFCIndexFactory.createFullIncrementalTieredStrategy(
            baseDefinitions,
            maxBitsPerDimension,
            sfcType,
            maxEstimatedDuplicatedIds);

    return new XZHierarchicalIndexStrategy(baseDefinitions, rasterStrategy, maxBitsPerDimension);
  }
}
