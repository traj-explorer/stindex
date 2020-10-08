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

import java.util.List;
import com.github.tm.stindex.dimension.NumericDimensionDefinition;
import com.github.tm.stindex.persist.PersistenceUtils;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;

public class PartitionIndexStrategyWrapper implements NumericIndexStrategy {
  private PartitionIndexStrategy<MultiDimensionalNumericData, MultiDimensionalNumericData> partitionIndexStrategy;

  public PartitionIndexStrategyWrapper() {}

  public PartitionIndexStrategyWrapper(
      final PartitionIndexStrategy<MultiDimensionalNumericData, MultiDimensionalNumericData> partitionIndexStrategy) {
    this.partitionIndexStrategy = partitionIndexStrategy;
  }

  @Override
  public QueryRanges getQueryRanges(
      final MultiDimensionalNumericData indexedRange,
      final IndexMetaData... hints) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public QueryRanges getQueryRanges(
      final MultiDimensionalNumericData indexedRange,
      final int maxEstimatedRangeDecomposition,
      final IndexMetaData... hints) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public InsertionIds getInsertionIds(final MultiDimensionalNumericData indexedData) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public InsertionIds getInsertionIds(
      final MultiDimensionalNumericData indexedData,
      final int maxEstimatedDuplicateIds) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MultiDimensionalNumericData getRangeForId(
      final byte[] partitionKey,
      final byte[] sortKey) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getId() {
    return partitionIndexStrategy.getId();
  }

  @Override
  public List<IndexMetaData> createMetaData() {
    return partitionIndexStrategy.createMetaData();
  }

  @Override
  public byte[] toBinary() {
    return PersistenceUtils.toBinary(partitionIndexStrategy);
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    partitionIndexStrategy =
        (PartitionIndexStrategy<MultiDimensionalNumericData, MultiDimensionalNumericData>) PersistenceUtils.fromBinary(
            bytes);
  }

  @Override
  public MultiDimensionalCoordinates getCoordinatesPerDimension(
      final byte[] partitionKey,
      final byte[] sortKey) {
    return new MultiDimensionalCoordinates();
  }

  @Override
  public MultiDimensionalCoordinateRanges[] getCoordinateRangesPerDimension(
      final MultiDimensionalNumericData dataRange,
      final IndexMetaData... hints) {
    return null;
  }

  @Override
  public NumericDimensionDefinition[] getOrderedDimensionDefinitions() {
    return null;
  }

  @Override
  public double[] getHighestPrecisionIdRangePerDimension() {
    return null;
  }

  @Override
  public int getPartitionKeyLength() {
    return partitionIndexStrategy.getPartitionKeyLength();
  }

  @Override
  public byte[][] getInsertionPartitionKeys(final MultiDimensionalNumericData insertionData) {
    return partitionIndexStrategy.getInsertionPartitionKeys(insertionData);
  }

  @Override
  public byte[][] getQueryPartitionKeys(
      final MultiDimensionalNumericData queryData,
      final IndexMetaData... hints) {
    return partitionIndexStrategy.getQueryPartitionKeys(queryData, hints);
  }

  @Override
  public byte[][] getPredefinedSplits() {
    return partitionIndexStrategy.getPredefinedSplits();
  }
}
