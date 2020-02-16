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

import java.nio.ByteBuffer;
import java.util.List;
import com.github.stdi_sys.stindex.dimension.NumericDimensionDefinition;
import com.github.stdi_sys.stindex.persist.PersistenceUtils;
import com.github.stdi_sys.stindex.spatial.sfc.data.MultiDimensionalNumericData;

public class NumericIndexStrategyWrapper implements NumericIndexStrategy {
  private String id;
  private NumericIndexStrategy indexStrategy;

  protected NumericIndexStrategyWrapper() {}

  public NumericIndexStrategyWrapper(final String id, final NumericIndexStrategy indexStrategy) {
    this.id = id;
    this.indexStrategy = indexStrategy;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public byte[] toBinary() {
    final byte[] idBinary = StringUtils.stringToBinary(id);
    final byte[] delegateBinary = PersistenceUtils.toBinary(indexStrategy);
    final ByteBuffer buf =
        ByteBuffer.allocate(
            VarintUtils.unsignedIntByteLength(idBinary.length)
                + idBinary.length
                + delegateBinary.length);
    VarintUtils.writeUnsignedInt(idBinary.length, buf);
    buf.put(idBinary);
    buf.put(delegateBinary);
    return buf.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    final int idBinaryLength = VarintUtils.readUnsignedInt(buf);
    final byte[] idBinary = ByteArrayUtils.safeRead(buf, idBinaryLength);
    final byte[] delegateBinary = new byte[buf.remaining()];
    buf.get(delegateBinary);
    id = StringUtils.stringFromBinary(idBinary);
    indexStrategy = (NumericIndexStrategy) PersistenceUtils.fromBinary(delegateBinary);
  }

  @Override
  public QueryRanges getQueryRanges(
      final MultiDimensionalNumericData indexedRange,
      final IndexMetaData... hints) {
    return indexStrategy.getQueryRanges(indexedRange, hints);
  }

  @Override
  public QueryRanges getQueryRanges(
      final MultiDimensionalNumericData indexedRange,
      final int maxRangeDecomposition,
      final IndexMetaData... hints) {
    return indexStrategy.getQueryRanges(indexedRange, maxRangeDecomposition, hints);
  }

  @Override
  public InsertionIds getInsertionIds(final MultiDimensionalNumericData indexedData) {
    return indexStrategy.getInsertionIds(indexedData);
  }

  @Override
  public MultiDimensionalNumericData getRangeForId(
      final byte[] partitionKey,
      final byte[] sortKey) {
    return indexStrategy.getRangeForId(partitionKey, sortKey);
  }

  @Override
  public MultiDimensionalCoordinates getCoordinatesPerDimension(
      final byte[] partitionKey,
      final byte[] sortKey) {
    return indexStrategy.getCoordinatesPerDimension(partitionKey, sortKey);
  }

  @Override
  public NumericDimensionDefinition[] getOrderedDimensionDefinitions() {
    return indexStrategy.getOrderedDimensionDefinitions();
  }

  @Override
  public double[] getHighestPrecisionIdRangePerDimension() {
    return indexStrategy.getHighestPrecisionIdRangePerDimension();
  }

  @Override
  public InsertionIds getInsertionIds(
      final MultiDimensionalNumericData indexedData,
      final int maxDuplicateInsertionIds) {
    return indexStrategy.getInsertionIds(indexedData, maxDuplicateInsertionIds);
  }

  @Override
  public int getPartitionKeyLength() {
    return indexStrategy.getPartitionKeyLength();
  }

  @Override
  public List<IndexMetaData> createMetaData() {
    return indexStrategy.createMetaData();
  }

  @Override
  public MultiDimensionalCoordinateRanges[] getCoordinateRangesPerDimension(
      final MultiDimensionalNumericData dataRange,
      final IndexMetaData... hints) {
    return indexStrategy.getCoordinateRangesPerDimension(dataRange, hints);
  }

  @Override
  public byte[][] getInsertionPartitionKeys(final MultiDimensionalNumericData insertionData) {
    return indexStrategy.getInsertionPartitionKeys(insertionData);
  }

  @Override
  public byte[][] getQueryPartitionKeys(
      final MultiDimensionalNumericData queryData,
      final IndexMetaData... hints) {
    return indexStrategy.getQueryPartitionKeys(queryData, hints);
  }

  @Override
  public byte[][] getPredefinedSplits() {
    return indexStrategy.getPredefinedSplits();
  }
}
