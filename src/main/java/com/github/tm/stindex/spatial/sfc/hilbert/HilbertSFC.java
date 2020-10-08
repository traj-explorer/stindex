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
package com.github.tm.stindex.spatial.sfc.hilbert;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

import com.github.tm.stindex.*;
import com.github.tm.stindex.persist.PersistenceUtils;
import com.github.tm.stindex.spatial.GridIndex;
import com.github.tm.stindex.spatial.sfc.RangeDecomposition;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.google.uzaygezen.core.CompactHilbertCurve;
import com.google.uzaygezen.core.MultiDimensionalSpec;

/**
 * Implementation of a Compact Hilbert space filling curve.
 * In a Compact Hilbert SFC, precision of each dimension can be different.
 *
 * @author Yu Liebing
 * */
public class HilbertSFC implements GridIndex {

  private static class QueryCacheKey {
    private final double[] minsPerDimension;
    private final double[] maxesPerDimension;
    private final boolean overInclusiveOnEdge;
    private final int maxFilteredIndexedRanges;

    public QueryCacheKey(
        final double[] minsPerDimension,
        final double[] maxesPerDimension,
        final boolean overInclusiveOnEdge,
        final int maxFilteredIndexedRanges) {
      this.minsPerDimension = minsPerDimension;
      this.maxesPerDimension = maxesPerDimension;
      this.overInclusiveOnEdge = overInclusiveOnEdge;
      this.maxFilteredIndexedRanges = maxFilteredIndexedRanges;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + maxFilteredIndexedRanges;
      result = (prime * result) + Arrays.hashCode(maxesPerDimension);
      result = (prime * result) + Arrays.hashCode(minsPerDimension);
      result = (prime * result) + (overInclusiveOnEdge ? 1231 : 1237);
      return result;
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
      final QueryCacheKey other = (QueryCacheKey) obj;
      if (maxFilteredIndexedRanges != other.maxFilteredIndexedRanges) {
        return false;
      }
      if (!Arrays.equals(maxesPerDimension, other.maxesPerDimension)) {
        return false;
      }
      if (!Arrays.equals(minsPerDimension, other.minsPerDimension)) {
        return false;
      }
      if (overInclusiveOnEdge != other.overInclusiveOnEdge) {
        return false;
      }
      return true;
    }
  }

  private static final int MAX_CACHED_QUERIES = 500;
  private final Map<QueryCacheKey, RangeDecomposition> queryDecompositionCache =
      new LinkedHashMap<QueryCacheKey, RangeDecomposition>(MAX_CACHED_QUERIES + 1, .75F, true) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean removeEldestEntry(
            final Map.Entry<QueryCacheKey, RangeDecomposition> eldest) {
          return size() > MAX_CACHED_QUERIES;
        }
      };

  protected CompactHilbertCurve compactHilbertCurve;
  protected SFCDimensionDefinition[] dimensionDefinitions;
  protected int totalPrecision;
  protected int res;

  /** Tunables * */
  private static final boolean REMOVE_VACUUM = true;

  protected HilbertSFCOperations getIdOperations;
  protected HilbertSFCOperations decomposeQueryOperations;

  public HilbertSFC() {
    super();
  }

  /**
   * Use the {@code SFCFactory.createSpaceFillingCurve} method - don't call this constructor directly
   * */
  public HilbertSFC(final SFCDimensionDefinition[] dimensionDefs) {
    init(dimensionDefs);
  }

  protected void init(final SFCDimensionDefinition[] dimensionDefs) {
    final List<Integer> bitsPerDimension = new ArrayList<>();
    totalPrecision = 0;
    for (final SFCDimensionDefinition dimension : dimensionDefs) {
      bitsPerDimension.add(dimension.getBitsOfPrecision());
      totalPrecision += dimension.getBitsOfPrecision();
      res = dimension.getBitsOfPrecision() > res ? dimension.getBitsOfPrecision() : res;
    }
    compactHilbertCurve = new CompactHilbertCurve(new MultiDimensionalSpec(bitsPerDimension));
    dimensionDefinitions = dimensionDefs;
    setOptimalOperations(totalPrecision, bitsPerDimension, dimensionDefs);
  }

  protected void setOptimalOperations(
      final int totalPrecision,
      final List<Integer> bitsPerDimension,
      final SFCDimensionDefinition[] dimensionDefs) {
    boolean primitiveForGetId = true;
    final boolean primitiveForQueryDecomposition = totalPrecision <= 62L;
    for (final Integer bits : bitsPerDimension) {
      if (bits > 48) {
        // if in any one dimension, more than 48 bits are used, we need
        // to use bigdecimals
        primitiveForGetId = false;
        break;
      }
    }
    if (primitiveForGetId) {
      final PrimitiveHilbertSFCOperations primitiveOps = new PrimitiveHilbertSFCOperations();
      primitiveOps.init(dimensionDefs);
      getIdOperations = primitiveOps;
      if (primitiveForQueryDecomposition) {
        decomposeQueryOperations = primitiveOps;
      } else {
        final UnboundedHilbertSFCOperations unboundedOps = new UnboundedHilbertSFCOperations();
        unboundedOps.init(dimensionDefs);
        decomposeQueryOperations = unboundedOps;
      }
    } else {
      final UnboundedHilbertSFCOperations unboundedOps = new UnboundedHilbertSFCOperations();
      unboundedOps.init(dimensionDefs);
      getIdOperations = unboundedOps;
      if (primitiveForQueryDecomposition) {
        final PrimitiveHilbertSFCOperations primitiveOps = new PrimitiveHilbertSFCOperations();
        primitiveOps.init(dimensionDefs);
        decomposeQueryOperations = primitiveOps;
      } else {
        decomposeQueryOperations = unboundedOps;
      }
    }
  }

  /** * {@inheritDoc} */
  @Override
  public ByteArray getIndex(final double[] values) {
    byte[] index = getIdOperations.convertToHilbert(values, compactHilbertCurve, dimensionDefinitions);
    return new SpatialByteArray(index, res);
  }

  @Override
  public MultiDimensionalNumericData getRanges(ByteArrayRange indexRange) {
    return null;
  }

  /** * {@inheritDoc} */
  @Override
  public RangeDecomposition decomposeRangeFully(final MultiDimensionalNumericData query) {
    return decomposeRange(query, -1);
  }

  // TODO: improve this method - min/max not being calculated optimally
  /** * {@inheritDoc} */
  @Override
  public RangeDecomposition decomposeRange(
      final MultiDimensionalNumericData query,
      int maxFilteredIndexedRanges) {
    // we always don't need to over inclusive
    boolean overInclusiveOnEdge = false;
    if (maxFilteredIndexedRanges == -1) {
      maxFilteredIndexedRanges = Integer.MAX_VALUE;
    }
    final QueryCacheKey key =
        new QueryCacheKey(
            query.getMinValuesPerDimension(),
            query.getMaxValuesPerDimension(),
            overInclusiveOnEdge,
            maxFilteredIndexedRanges);
    RangeDecomposition rangeDecomp = queryDecompositionCache.get(key);
    if (rangeDecomp == null) {
      rangeDecomp =
          decomposeQueryOperations.decomposeRange(
              query.getDataPerDimension(),
              compactHilbertCurve,
              dimensionDefinitions,
              totalPrecision,
              maxFilteredIndexedRanges,
              REMOVE_VACUUM,
              overInclusiveOnEdge);
      queryDecompositionCache.put(key, rangeDecomp);
    }
    return rangeDecomp;
  }

  protected static byte[] fitExpectedByteCount(final int expectedByteCount, final byte[] bytes) {
    final int leftPadding = expectedByteCount - bytes.length;
    if (leftPadding > 0) {
      final byte[] zeroes = new byte[leftPadding];
      Arrays.fill(zeroes, (byte) 0);
      return ByteArrayUtils.combineArrays(zeroes, bytes);
    } else if (leftPadding < 0) {
      final byte[] truncatedBytes = new byte[expectedByteCount];

      if (bytes[0] != 0) {
        Arrays.fill(truncatedBytes, (byte) 255);
      } else {
        System.arraycopy(bytes, -leftPadding, truncatedBytes, 0, expectedByteCount);
      }
      return truncatedBytes;
    }
    return bytes;
  }

  @Override
  public byte[] toBinary() {
    final List<byte[]> dimensionDefBinaries = new ArrayList<>(dimensionDefinitions.length);
    int bufferLength = 0;
    for (final SFCDimensionDefinition sfcDimension : dimensionDefinitions) {
      final byte[] sfcDimensionBinary = PersistenceUtils.toBinary(sfcDimension);
      bufferLength +=
          (sfcDimensionBinary.length
              + VarintUtils.unsignedIntByteLength(sfcDimensionBinary.length));
      dimensionDefBinaries.add(sfcDimensionBinary);
    }
    bufferLength += VarintUtils.unsignedIntByteLength(dimensionDefinitions.length);
    final ByteBuffer buf = ByteBuffer.allocate(bufferLength);
    VarintUtils.writeUnsignedInt(dimensionDefinitions.length, buf);
    for (final byte[] dimensionDefBinary : dimensionDefBinaries) {
      VarintUtils.writeUnsignedInt(dimensionDefBinary.length, buf);
      buf.put(dimensionDefBinary);
    }
    return buf.array();
  }

  @Override
  public void fromBinary(final byte[] bytes) {
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    final int numDimensions = VarintUtils.readUnsignedInt(buf);
    dimensionDefinitions = new SFCDimensionDefinition[numDimensions];
    for (int i = 0; i < numDimensions; i++) {
      final byte[] dim = ByteArrayUtils.safeRead(buf, VarintUtils.readUnsignedInt(buf));
      dimensionDefinitions[i] = (SFCDimensionDefinition) PersistenceUtils.fromBinary(dim);
    }
    init(dimensionDefinitions);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    final String className = getClass().getName();
    result = (prime * result) + ((className == null) ? 0 : className.hashCode());
    result = (prime * result) + Arrays.hashCode(dimensionDefinitions);
    return result;
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
    final HilbertSFC other = (HilbertSFC) obj;

    if (!Arrays.equals(dimensionDefinitions, other.dimensionDefinitions)) {
      return false;
    }
    return true;
  }

  @Override
  public BigInteger getEstimatedIdCount(final MultiDimensionalNumericData data) {
    return getIdOperations.getEstimatedIdCount(data, dimensionDefinitions);
  }

  @Override
  public MultiDimensionalNumericData getRanges(final ByteArray index) {
    return getIdOperations.convertFromHilbert(index.getBytes(), compactHilbertCurve, dimensionDefinitions);
  }

  @Override
  public long[] normalizeRange(final double minValue, final double maxValue, final int dimension) {
    return getIdOperations.normalizeRange(
        minValue,
        maxValue,
        dimension,
        dimensionDefinitions[dimension]);
  }

  @Override
  public long[] getCoordinates(final ByteArray index) {
    return getIdOperations.indicesFromHilbert(index.getBytes(), compactHilbertCurve, dimensionDefinitions);
  }

  @Override
  public double[] getInsertionIdRangePerDimension() {
    return getIdOperations.getInsertionIdRangePerDimension(dimensionDefinitions);
  }

  @Override
  public ByteArrayRange getFatherRange(final ByteArrayRange childRange) {
    int commonBitsNum = ByteArrayUtils.commonBitsNum(
            childRange.getStart().getBytes(),
            childRange.getEnd().getBytes());
    int dims = dimensionDefinitions.length;
    commonBitsNum = commonBitsNum / dims * dims;
    ByteArray commonBits = ByteArrayUtils.commonPrefix(
            childRange.getStart().getBytes(),
            childRange.getEnd().getBytes());
    int offset = commonBits.length() * 8 - totalPrecision;
    int start = commonBitsNum > offset ? commonBitsNum - dims : commonBitsNum;
    byte[] fatherStartBytes = new byte[commonBits.length()];
    byte[] fatherEndBytes = new byte[commonBits.length()];
    System.arraycopy(commonBits.getBytes(), 0, fatherStartBytes, 0, commonBits.length());
    System.arraycopy(commonBits.getBytes(), 0, fatherEndBytes, 0, commonBits.length());
    for (int i = start; i < commonBits.length() * 8; i++) {
      fatherStartBytes[i / 8] &= 0xff << (8 - i % 8);
      fatherEndBytes[i / 8] |= 1 << (7 - i % 8);
    }
    int curRes = childRange.getStart().getRes();
    return new ByteArrayRange(new SpatialByteArray(fatherStartBytes, curRes - 1),
                              new SpatialByteArray(fatherEndBytes, curRes - 1));
  }

  @Override
  public ByteArrayRange[] getRing(ByteArrayRange center) {
    int dims = dimensionDefinitions.length;

    int curRes = center.getStart().getRes();
    int originOffset = center.getStart().length() * 8 - totalPrecision;
    int curByteLen = (curRes * 2 + 7) / 8;
    int curOffset = curByteLen * 8 - curRes * 2;
    // get the hilbert value on current res
    BitSet originBitSet = BitSet.valueOf(ByteArrayUtils.swapEndianFormat(center.getStart().getBytes()));
    BitSet curBitSet = new BitSet(curByteLen * 8);
    for (int i = curOffset; i < curByteLen * 8; i++) {
      curBitSet.set(i, originBitSet.get(originOffset + i - curOffset));
    }
    byte[] centerIndex = ByteArrayUtils.swapEndianFormat(curBitSet.toByteArray());
    if (centerIndex.length < curByteLen) {
      centerIndex = Arrays.copyOf(centerIndex, curByteLen);
    }
    // get the coordinate
    List<Integer> bitsPerDimension = new ArrayList<>();
    for (int i = 0; i < dims; i++) {
      bitsPerDimension.add(center.getStart().getRes());
    }
    CompactHilbertCurve compactHilbertCurve = new CompactHilbertCurve(new MultiDimensionalSpec(bitsPerDimension));
    long[] coordinates = getIdOperations.indicesFromHilbert(
            centerIndex, compactHilbertCurve, dims, center.getStart().getRes());
    // get ring
    // TODO: support n dimensions
    List<ByteArrayRange> retVal = new ArrayList<>(8);
    long maxHilbert = (long) (Math.pow(2, curRes) - 1);
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i == 0 && j == 0) continue;
          long x = coordinates[0] + i;
          long y = coordinates[1] + j;
          if (x < 0 || y < 0 || x > maxHilbert || y > maxHilbert) continue;
          byte[] index = getIdOperations.convertToHilbert(
                  new long[] {x, y},
                  compactHilbertCurve,
                  dims,
                  curRes);
          ByteArrayRange spatialRange = SpatialByteArray.transform(new SpatialByteArray(index, curRes), res);
          retVal.add(spatialRange);
      }
    }
    return ByteArrayRange.mergeIntersections(retVal,
            ByteArrayRange.MergeOperation.UNION).toArray(new ByteArrayRange[0]);
  }
}
