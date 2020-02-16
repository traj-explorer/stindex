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
package com.github.stdi_sys.stindex.spatial.sfc.xz;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.github.stdi_sys.stindex.*;
import com.github.stdi_sys.stindex.dimension.NumericDimensionDefinition;
import com.github.stdi_sys.stindex.persist.PersistenceUtils;
import com.github.stdi_sys.stindex.spatial.GridIndex;
import com.github.stdi_sys.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.stdi_sys.stindex.spatial.sfc.SFCFactory;
import com.github.stdi_sys.stindex.spatial.sfc.binned.BinnedSFCUtils;
import com.github.stdi_sys.stindex.spatial.sfc.tiered.TieredSFCIndexStrategy;
import com.github.stdi_sys.stindex.spatial.sfc.data.BinnedNumericDataset;
import com.github.stdi_sys.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class XZHierarchicalIndexStrategy implements HierarchicalNumericIndexStrategy {
  private static final Logger LOGGER = LoggerFactory.getLogger(XZHierarchicalIndexStrategy.class);

  protected static final int DEFAULT_MAX_RANGES = -1;

  private Byte pointCurveMultiDimensionalId = null;
  private Byte xzCurveMultiDimensionalId = null;

  private GridIndex pointCurve;
  private GridIndex xzCurve;
  private TieredSFCIndexStrategy rasterStrategy;

  private NumericDimensionDefinition[] baseDefinitions;
  private int[] maxBitsPerDimension;

  private int byteOffsetFromDimensionIndex;

  public XZHierarchicalIndexStrategy() {}

  /**
   * Constructor used to create a XZ Hierarchical Index Strategy.
   *
   * @param maxBitsPerDimension
   */
  public XZHierarchicalIndexStrategy(
      final NumericDimensionDefinition[] baseDefinitions,
      final TieredSFCIndexStrategy rasterStrategy,
      final int[] maxBitsPerDimension) {
    this.rasterStrategy = rasterStrategy;
    this.maxBitsPerDimension = maxBitsPerDimension;
    init(baseDefinitions);
  }

  private void init(final NumericDimensionDefinition[] baseDefinitions) {

    this.baseDefinitions = baseDefinitions;

    byteOffsetFromDimensionIndex = rasterStrategy.getPartitionKeyLength();

    // init dimensionalIds with values not used by rasterStrategy
    for (byte i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
      if (!rasterStrategy.tierExists(i)) {
        if (pointCurveMultiDimensionalId == null) {
          pointCurveMultiDimensionalId = i;
        } else if (xzCurveMultiDimensionalId == null) {
          xzCurveMultiDimensionalId = i;
        } else {
          break;
        }
      }
    }
    if ((pointCurveMultiDimensionalId == null) || (xzCurveMultiDimensionalId == null)) {
      LOGGER.error("No available byte values for xz and point sfc multiDimensionalIds.");
    }

    final SFCDimensionDefinition[] sfcDimensions =
        new SFCDimensionDefinition[baseDefinitions.length];
    for (int i = 0; i < baseDefinitions.length; i++) {
      sfcDimensions[i] = new SFCDimensionDefinition(baseDefinitions[i], maxBitsPerDimension[i]);
    }

    pointCurve = SFCFactory.createSpaceFillingCurve(sfcDimensions, SFCFactory.SFCType.HILBERT);
    xzCurve = SFCFactory.createSpaceFillingCurve(sfcDimensions, SFCFactory.SFCType.XZORDER);
  }

  @Override
  public QueryRanges getQueryRanges(
      final MultiDimensionalNumericData indexedRange,
      final IndexMetaData... hints) {
    return getQueryRanges(indexedRange, DEFAULT_MAX_RANGES, hints);
  }

  @Override
  public QueryRanges getQueryRanges(
      final MultiDimensionalNumericData indexedRange,
      final int maxEstimatedRangeDecomposition,
      final IndexMetaData... hints) {

    // TODO don't just pass max ranges along to the SFC, take tiering and
    // binning into account to limit the number of ranges correctly

    TieredSFCIndexStrategy.TierIndexMetaData tieredHints = null;
    XZHierarchicalIndexMetaData xzHints = null;
    if ((hints != null) && (hints.length > 0)) {
      tieredHints = (TieredSFCIndexStrategy.TierIndexMetaData) hints[0];
      xzHints = (XZHierarchicalIndexMetaData) hints[1];
    }
    final QueryRanges queryRanges =
        rasterStrategy.getQueryRanges(indexedRange, maxEstimatedRangeDecomposition, tieredHints);

    final List<BinnedNumericDataset> binnedQueries =
        BinnedNumericDataset.applyBins(indexedRange, baseDefinitions);
    final List<SinglePartitionQueryRanges> partitionedRanges = new ArrayList<>();
    if ((xzHints == null) || (xzHints.pointCurveCount > 0)) {
      partitionedRanges.addAll(
          BinnedSFCUtils.getQueryRanges(
              binnedQueries,
              pointCurve,
              maxEstimatedRangeDecomposition, // for
              // now
              // we're
              // doing this
              // per SFC rather
              // than
              // dividing by the
              // SFCs
              pointCurveMultiDimensionalId));
    }

    if ((xzHints == null) || (xzHints.xzCurveCount > 0)) {
      partitionedRanges.addAll(
          BinnedSFCUtils.getQueryRanges(
              binnedQueries,
              xzCurve,
              maxEstimatedRangeDecomposition, // for
              // now
              // we're
              // doing this
              // per SFC rather
              // than
              // dividing by the
              // SFCs
              xzCurveMultiDimensionalId));
    }
    if (partitionedRanges.isEmpty()) {
      return queryRanges;
    }
    final List<QueryRanges> queryRangesList = new ArrayList<>();
    queryRangesList.add(queryRanges);
    queryRangesList.add(new QueryRanges(partitionedRanges));
    return new QueryRanges(queryRangesList);
  }

  @Override
  public InsertionIds getInsertionIds(final MultiDimensionalNumericData indexedData) {

    final List<BinnedNumericDataset> ranges =
        BinnedNumericDataset.applyBins(indexedData, baseDefinitions);
    final List<SinglePartitionInsertionIds> partitionIds = new ArrayList<>(ranges.size());

    for (final BinnedNumericDataset range : ranges) {
      final BigInteger pointIds = pointCurve.getEstimatedIdCount(range);
      final SinglePartitionInsertionIds pointCurveId =
          BinnedSFCUtils.getSingleBinnedInsertionId(
              pointIds,
              pointCurveMultiDimensionalId,
              range,
              pointCurve);
      if (pointCurveId != null) {
        partitionIds.add(pointCurveId);
      } else {
        final double[] mins = range.getMinValuesPerDimension();
        final double[] maxes = range.getMaxValuesPerDimension();

        final double[] values = new double[mins.length + maxes.length];
        for (int i = 0; i < (values.length - 1); i++) {
          values[i] = mins[i / 2];
          values[i + 1] = maxes[i / 2];
          i++;
        }

        final byte[] xzId = xzCurve.getIndex(values).getBytes();

        partitionIds.add(
            new SinglePartitionInsertionIds(
                ByteArrayUtils.combineArrays(
                    new byte[] {xzCurveMultiDimensionalId},
                    range.getBinId()),
                xzId));
      }
    }

    return new InsertionIds(partitionIds);
  }

  @Override
  public InsertionIds getInsertionIds(
      final MultiDimensionalNumericData indexedData,
      final int maxEstimatedDuplicateIds) {
    return getInsertionIds(indexedData);
  }

  @Override
  public MultiDimensionalNumericData getRangeForId(
      final byte[] partitionKey,
      final byte[] sortKey) {
    // select curve based on first byte
    final byte first = partitionKey[0];
    if (first == pointCurveMultiDimensionalId) {
      return pointCurve.getRanges(new ByteArray(sortKey));
    } else if (first == xzCurveMultiDimensionalId) {
      return xzCurve.getRanges(new ByteArray(sortKey));
    } else {
      return rasterStrategy.getRangeForId(partitionKey, sortKey);
    }
  }

  @Override
  public int hashCode() {
    // internal tiered raster strategy already contains all the details that
    // provide uniqueness and comparability to the hierarchical strategy
    return rasterStrategy.hashCode();
  }

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
    final XZHierarchicalIndexStrategy other = (XZHierarchicalIndexStrategy) obj;
    // internal tiered raster strategy already contains all the details that
    // provide uniqueness and comparability to the hierarchical strategy
    return rasterStrategy.equals(other.rasterStrategy);
  }

  @Override
  public String getId() {
    return StringUtils.intToString(hashCode());
  }

  @Override
  public byte[] toBinary() {

    final List<byte[]> dimensionDefBinaries = new ArrayList<>(baseDefinitions.length);
    int bufferLength = VarintUtils.unsignedIntByteLength(baseDefinitions.length);
    for (final NumericDimensionDefinition dimension : baseDefinitions) {
      final byte[] sfcDimensionBinary = PersistenceUtils.toBinary(dimension);
      bufferLength +=
          (sfcDimensionBinary.length
              + VarintUtils.unsignedIntByteLength(sfcDimensionBinary.length));
      dimensionDefBinaries.add(sfcDimensionBinary);
    }

    final byte[] rasterStrategyBinary = PersistenceUtils.toBinary(rasterStrategy);
    bufferLength +=
        VarintUtils.unsignedIntByteLength(rasterStrategyBinary.length)
            + rasterStrategyBinary.length;

    bufferLength += VarintUtils.unsignedIntByteLength(maxBitsPerDimension.length);
    bufferLength += maxBitsPerDimension.length * 4;

    final ByteBuffer buf = ByteBuffer.allocate(bufferLength);

    VarintUtils.writeUnsignedInt(baseDefinitions.length, buf);
    for (final byte[] dimensionDefBinary : dimensionDefBinaries) {
      VarintUtils.writeUnsignedInt(dimensionDefBinary.length, buf);
      buf.put(dimensionDefBinary);
    }

    VarintUtils.writeUnsignedInt(rasterStrategyBinary.length, buf);
    buf.put(rasterStrategyBinary);

    VarintUtils.writeUnsignedInt(maxBitsPerDimension.length, buf);
    for (final int dimBits : maxBitsPerDimension) {
      buf.putInt(dimBits);
    }

    return buf.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {

    final ByteBuffer buf = ByteBuffer.wrap(bytes);

    final int numDimensions = VarintUtils.readUnsignedInt(buf);

    baseDefinitions = new NumericDimensionDefinition[numDimensions];
    for (int i = 0; i < numDimensions; i++) {
      final byte[] dim = ByteArrayUtils.safeRead(buf, VarintUtils.readUnsignedInt(buf));
      baseDefinitions[i] = (NumericDimensionDefinition) PersistenceUtils.fromBinary(dim);
    }

    final int rasterStrategySize = VarintUtils.readUnsignedInt(buf);
    final byte[] rasterStrategyBinary = ByteArrayUtils.safeRead(buf, rasterStrategySize);
    rasterStrategy = (TieredSFCIndexStrategy) PersistenceUtils.fromBinary(rasterStrategyBinary);

    final int bitsPerDimensionLength = VarintUtils.readUnsignedInt(buf);
    maxBitsPerDimension = new int[bitsPerDimensionLength];
    for (int i = 0; i < bitsPerDimensionLength; i++) {
      maxBitsPerDimension[i] = buf.getInt();
    }

    init(baseDefinitions);
  }

  @Override
  public MultiDimensionalCoordinates getCoordinatesPerDimension(
      final byte[] partitionKey,
      final byte[] sortKey) {

    // select curve based on first byte
    final byte first = partitionKey[0];
    Coordinate[] coordinates = null;

    if (first == pointCurveMultiDimensionalId) {
      coordinates =
          BinnedSFCUtils.getCoordinatesForId(
              ByteArrayUtils.combineArrays(partitionKey, sortKey == null ? null : sortKey),
              baseDefinitions,
              pointCurve);
    } else if (first == xzCurveMultiDimensionalId) {
      coordinates =
          BinnedSFCUtils.getCoordinatesForId(
              ByteArrayUtils.combineArrays(partitionKey, sortKey == null ? null : sortKey),
              baseDefinitions,
              xzCurve);
    } else {
      return rasterStrategy.getCoordinatesPerDimension(partitionKey, sortKey);
    }
    if (coordinates == null) {
      return null;
    }
    return new MultiDimensionalCoordinates(new byte[] {first}, coordinates);
  }

  @Override
  public MultiDimensionalCoordinateRanges[] getCoordinateRangesPerDimension(
      final MultiDimensionalNumericData dataRange,
      final IndexMetaData... hints) {
    final MultiDimensionalCoordinateRanges[] rasterRanges =
        rasterStrategy.getCoordinateRangesPerDimension(dataRange, hints);

    // just pass through raster strategy results since this is only used by
    // raster data for now
    return rasterRanges;
  }

  @Override
  public NumericDimensionDefinition[] getOrderedDimensionDefinitions() {
    return baseDefinitions;
  }

  @Override
  public double[] getHighestPrecisionIdRangePerDimension() {
    return pointCurve.getInsertionIdRangePerDimension();
  }

  @Override
  public int getPartitionKeyLength() {
    return byteOffsetFromDimensionIndex;
  }

  @Override
  public SubStrategy[] getSubStrategies() {
    return rasterStrategy.getSubStrategies();
  }

  @Override
  public List<IndexMetaData> createMetaData() {
    final List<IndexMetaData> metaData = new ArrayList<>();
    metaData.addAll(rasterStrategy.createMetaData());
    metaData.add(
        new XZHierarchicalIndexMetaData(pointCurveMultiDimensionalId, xzCurveMultiDimensionalId));
    return metaData;
  }

  public static class XZHierarchicalIndexMetaData implements IndexMetaData {

    private int pointCurveCount = 0;
    private int xzCurveCount = 0;

    private byte pointCurveMultiDimensionalId;
    private byte xzCurveMultiDimensionalId;

    public XZHierarchicalIndexMetaData() {}

    public XZHierarchicalIndexMetaData(
        final byte pointCurveMultiDimensionalId,
        final byte xzCurveMultiDimensionalId) {
      super();
      this.pointCurveMultiDimensionalId = pointCurveMultiDimensionalId;
      this.xzCurveMultiDimensionalId = xzCurveMultiDimensionalId;
    }

    @Override
    public byte[] toBinary() {
      final ByteBuffer buffer =
          ByteBuffer.allocate(
              2
                  + VarintUtils.unsignedIntByteLength(pointCurveCount)
                  + VarintUtils.unsignedIntByteLength(xzCurveCount));
      buffer.put(pointCurveMultiDimensionalId);
      buffer.put(xzCurveMultiDimensionalId);
      VarintUtils.writeUnsignedInt(pointCurveCount, buffer);
      VarintUtils.writeUnsignedInt(xzCurveCount, buffer);
      return buffer.array();
    }

    @Override
    public void fromBinary(final byte[] bytes) {
      final ByteBuffer buffer = ByteBuffer.wrap(bytes);
      pointCurveMultiDimensionalId = buffer.get();
      xzCurveMultiDimensionalId = buffer.get();
      pointCurveCount = VarintUtils.readUnsignedInt(buffer);
      xzCurveCount = VarintUtils.readUnsignedInt(buffer);
    }

    @Override
    public void merge(final Mergeable merge) {
      if (merge instanceof XZHierarchicalIndexMetaData) {
        final XZHierarchicalIndexMetaData other = (XZHierarchicalIndexMetaData) merge;
        pointCurveCount += other.pointCurveCount;
        xzCurveCount += other.xzCurveCount;
      }
    }

    @Override
    public void insertionIdsAdded(final InsertionIds insertionIds) {
      for (final SinglePartitionInsertionIds partitionId : insertionIds.getPartitionKeys()) {
        final byte first = partitionId.getPartitionKey()[0];
        if (first == pointCurveMultiDimensionalId) {
          pointCurveCount += partitionId.getSortKeys().size();
        } else if (first == xzCurveMultiDimensionalId) {
          xzCurveCount += partitionId.getSortKeys().size();
        }
      }
    }

    @Override
    public void insertionIdsRemoved(final InsertionIds insertionIds) {
      for (final SinglePartitionInsertionIds partitionId : insertionIds.getPartitionKeys()) {
        final byte first = partitionId.getPartitionKey()[0];
        if (first == pointCurveMultiDimensionalId) {
          pointCurveCount -= partitionId.getSortKeys().size();
        } else if (first == xzCurveMultiDimensionalId) {
          xzCurveCount -= partitionId.getSortKeys().size();
        }
      }
    }

    /** Convert XZHierarchical Index Metadata statistics to a JSON object */
    @Override
    public JSONObject toJSONObject() throws JSONException {
      final JSONObject jo = new JSONObject();
      jo.put("type", "XZHierarchicalIndexStrategy");

      jo.put("pointCurveMultiDimensionalId", pointCurveMultiDimensionalId);
      jo.put("xzCurveMultiDimensionalId", xzCurveMultiDimensionalId);
      jo.put("pointCurveCount", pointCurveCount);
      jo.put("xzCurveCount", xzCurveCount);

      return jo;
    }
  }

  @Override
  public byte[][] getInsertionPartitionKeys(final MultiDimensionalNumericData insertionData) {
    return IndexUtils.getInsertionPartitionKeys(this, insertionData);
  }

  @Override
  public byte[][] getQueryPartitionKeys(
      final MultiDimensionalNumericData queryData,
      final IndexMetaData... hints) {
    return IndexUtils.getQueryPartitionKeys(this, queryData, hints);
  }
}
