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
package com.github.tm.stindex.spatial.sfc.zorder;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

import com.github.tm.stindex.ByteArrayRange;
import com.github.tm.stindex.ByteArrayUtils;
import com.github.tm.stindex.VarintUtils;
import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.persist.PersistenceUtils;
import com.github.tm.stindex.spatial.sfc.RangeDecomposition;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.GridIndex;
import com.github.tm.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import com.google.common.primitives.Ints;

/**
 * Implementation of a Z-order Space Filling Curve. Also called Morton, GeoHash, etc.
 * We support encoding of any number of dimensions, but the precision of each dimension must be the
 * same. If the input precision of each dimension is different, the maximum value of the precision of
 * all dimensions will be used as the precision of each dimension.
 *
 * @author Yu Liebing
 * */
public class ZOrderSFC implements GridIndex {
  private SFCDimensionDefinition[] dimensionDefs;
  // max bit precision of all dimensions
  private int cardinalityPerDimension;
  private double binsPerDimension;
  private int resolution;

  public ZOrderSFC() {
    super();
  }

  /**
   * Use the SFCFactory.createSpaceFillingCurve method - don't call this constructor directly
   * */
  public ZOrderSFC(final SFCDimensionDefinition[] dimensionDefs) {
    init(dimensionDefs);
  }

  private void init(final SFCDimensionDefinition[] dimensionDefs) {
    this.dimensionDefs = dimensionDefs;
    cardinalityPerDimension = 1;
    for (final SFCDimensionDefinition dimensionDef : dimensionDefs) {
      if (dimensionDef.getBitsOfPrecision() > cardinalityPerDimension) {
        cardinalityPerDimension = dimensionDef.getBitsOfPrecision();
      }
    }
    binsPerDimension = Math.pow(2, cardinalityPerDimension);
    resolution = cardinalityPerDimension;
  }

  @Override
  public ByteArray getIndex(final double[] values) {
    final double[] normalizedValues = new double[values.length];
    for (int d = 0; d < values.length; d++) {
      normalizedValues[d] = dimensionDefs[d].normalize(values[d]);
    }
    byte[] index = ZOrderUtils.encode(normalizedValues, cardinalityPerDimension, values.length);
    return new ByteArray(index);
  }

  @Override
  public MultiDimensionalNumericData getRanges(final ByteArray index) {
    return new BasicNumericDataset(
        ZOrderUtils.decodeRanges(index.getBytes(), cardinalityPerDimension, dimensionDefs));
  }

  @Override
  public MultiDimensionalNumericData getRanges(ByteArrayRange indexRange) {
    MultiDimensionalNumericData startRange = getRanges(indexRange.getStart());
    MultiDimensionalNumericData endRange = getRanges(indexRange.getEnd());
    double[] minValuesPerDim = startRange.getMinValuesPerDimension();
    double[] maxValuesPerDim = endRange.getMaxValuesPerDimension();
    int dims = startRange.getDimensionCount();
    NumericData[] ranges = new NumericData[dims];
    for (int i = 0; i < dims; i++) {
      NumericRange range = new NumericRange(minValuesPerDim[i], maxValuesPerDim[i]);
      ranges[i] = range;
    }
    return new BasicNumericDataset(ranges);
  }

  @Override
  public long[] getCoordinates(final ByteArray index) {
    return ZOrderUtils.decodeIndices(index.getBytes(), cardinalityPerDimension, dimensionDefs.length);
  }

  @Override
  public double[] getInsertionIdRangePerDimension() {
    final double[] retVal = new double[dimensionDefs.length];
    for (int i = 0; i < dimensionDefs.length; i++) {
      retVal[i] = dimensionDefs[i].getRange() / binsPerDimension;
    }
    return retVal;
  }

  @Override
  public ByteArrayRange getFatherRange(ByteArrayRange childRange) {
    return null;
  }

//  @Override
//  public ByteArray getFather(ByteArray child) {
//    int dims = dimensionDefs.length;
//    // to get father in ZOrder we only need to shift right dims bits and drop the last dim bits
//    byte[] littleEndian = ByteArrayUtils.swapEndianFormat(child.getBytes());
//    BitSet childBitSet = BitSet.valueOf(littleEndian);
//    int childBytes = (resolution * dims + 7) / 8;
//    int childBits = resolution * dims;
//    int childOffset = childBytes * 8 - childBits;
//    int fatherBytes = (resolution * dims - dims + 7) / 8;
//    int fatherBits = resolution * dims - dims;
//    int fatherOffset = fatherBytes * 8 - fatherBits;
//    BitSet fatherBitSet = new BitSet(fatherBytes * 8);
//    for (int i = 0; i < childBits - dims; i++) {
//      fatherBitSet.set(i + fatherOffset, childBitSet.get(i + childOffset));
//    }
//    byte[] littleEndianRes = fatherBitSet.toByteArray();
//    byte[] retVal = ByteArrayUtils.swapEndianFormat(littleEndianRes);
//    if (retVal.length < fatherBytes) {
//      retVal = Arrays.copyOf(retVal, fatherBytes);
//    }
//    return new ByteArray(retVal);
//  }

//  @Override
//  public ByteArrayRange getChildren(ByteArray father) {
//    int dims = dimensionDefs.length;
//    // the four children of a ZOrder cell is a continues range
//    BitSet fatherBitSet = BitSet.valueOf(ByteArrayUtils.swapEndianFormat(father.getBytes()));
//    int fatherBytes = (resolution * dims + 7) / 8;
//    int fatherBits = resolution * dims;
//    int fatherOffsetBits = fatherBytes * 8 - fatherBits;
//    int childrenBytes = (resolution * dims + dims + 7) / 8;
//    int childrenBits = resolution * dims + dims;
//    int childrenOffset = childrenBytes * 8 - childrenBits;
//    BitSet childrenBitSet = new BitSet(childrenBytes * 8);
//    for (int i = 0; i < fatherBits; i++) {
//      childrenBitSet.set(i + childrenOffset, fatherBitSet.get(i + fatherOffsetBits));
//    }
//    byte[] start = ByteArrayUtils.swapEndianFormat(childrenBitSet.toByteArray());
//    childrenBitSet.set(childrenBytes * 8 - dims, childrenBytes * 8);
//    byte[] end = ByteArrayUtils.swapEndianFormat(childrenBitSet.toByteArray());
//    if (start.length < childrenBytes) {
//      start = Arrays.copyOf(start, childrenBytes);
//    }
//    if (end.length < childrenBytes) {
//      end = Arrays.copyOf(end, childrenBytes);
//    }
//    return new ByteArrayRange(start, end);
//  }

  @Override
  public ByteArrayRange[] getRing(ByteArrayRange center) {


    return new ByteArrayRange[0];
  }

  @Override
  public BigInteger getEstimatedIdCount(final MultiDimensionalNumericData data) {
    final double[] mins = data.getMinValuesPerDimension();
    final double[] maxes = data.getMaxValuesPerDimension();
    BigInteger estimatedIdCount = BigInteger.valueOf(1);
    for (int d = 0; d < data.getDimensionCount(); d++) {
      final double binMin = dimensionDefs[d].normalize(mins[d]) * binsPerDimension;
      final double binMax = dimensionDefs[d].normalize(maxes[d]) * binsPerDimension;
      estimatedIdCount =
          estimatedIdCount.multiply(BigInteger.valueOf((long) (Math.abs(binMax - binMin) + 1)));
    }
    return estimatedIdCount;
  }

  /** * {@inheritDoc} */
  @Override
  public RangeDecomposition decomposeRange(
      final MultiDimensionalNumericData query,
      final int maxFilteredIndexedRanges) {
    List<ByteArrayRange> ranges = internalDecomposeRange(query, maxFilteredIndexedRanges);
    ByteArrayRange[] rangesArr = ranges.toArray(new ByteArrayRange[0]);
    return new RangeDecomposition(rangesArr);
  }

  /** * {@inheritDoc} */
  @Override
  public RangeDecomposition decomposeRangeFully(final MultiDimensionalNumericData query) {
    return decomposeRange(query, resolution);
  }

  public List<ByteArrayRange> internalDecomposeRange(
          final MultiDimensionalNumericData query,
          final int maxRecursive) {
    // get the min and max zOrder values
    final double[] queryMines = query.getMinValuesPerDimension();
    final double[] queryMaxes = query.getMaxValuesPerDimension();
    final ByteArray minZOrder = getIndex(queryMines);
    final ByteArray maxZOrder = getIndex(queryMaxes);
    ByteArrayRange bounds = new ByteArrayRange(minZOrder, maxZOrder);
    // get common prefix bits of min and max zOrder values
    List<ByteArrayRange> ranges = new ArrayList<>(100);
    ArrayDeque<ByteArrayRange> remaining = new ArrayDeque<>(100);
    int dims = dimensionDefs.length;
    int commonBitsNum = ByteArrayUtils.commonBitsNum(minZOrder.getBytes(), maxZOrder.getBytes());
    commonBitsNum = commonBitsNum / dims * dims;
    ByteArray commonPrefix = ByteArrayUtils.commonPrefix(minZOrder.getBytes(), maxZOrder.getBytes());
    int leftOffset = commonPrefix.getBytes().length * 8 - commonBitsNum;
    // get ranges top down
    int level = 0;
    remaining.add(bounds);
    remaining.add(ByteArrayRange.EMPTY_BYTE_ARRAY_RANGE);
    while (level < maxRecursive && !remaining.isEmpty() && leftOffset >= dims) {
      ByteArrayRange next = remaining.poll();
      if (next.equals(ByteArrayRange.EMPTY_BYTE_ARRAY_RANGE)) {
        if (!remaining.isEmpty()) {
          level += 1;
          leftOffset -= dims;
          remaining.add(ByteArrayRange.EMPTY_BYTE_ARRAY_RANGE);
        }
      } else {
        ByteArray prefix = next.getStart();
        int quad = 0;
        while (quad < (1 << dims) ) {
          ByteArrayRange childrenRange = getChildrenRange(prefix, leftOffset, dims, quad);
          MultiDimensionalNumericData range = getRanges(childrenRange);
          if (query.contains(range)) {
            ranges.add(childrenRange);
          } else if (query.intersects(range)) {
            remaining.add(childrenRange);
          }
          quad++;
        }
      }
    }
    // add the remaining ranges
    while (!remaining.isEmpty()) {
      ByteArrayRange range = remaining.poll();
      if (!range.equals(ByteArrayRange.EMPTY_BYTE_ARRAY_RANGE)) {
        ranges.add(range);
      }
    }
    return (List<ByteArrayRange>)ByteArrayRange.mergeIntersections(ranges, ByteArrayRange.MergeOperation.UNION);
  }

  public ByteArrayRange getChildrenRange(final ByteArray commonPrefix,
                                          final int leftOffset,
                                          final int dims,
                                          final int quad) {
    byte[] bytes = commonPrefix.getBytes().clone();
    BitSet prefixBitSet = BitSet.valueOf(ByteArrayUtils.swapEndianFormat(bytes));
    BitSet quadBitSet = BitSet.valueOf(ByteArrayUtils.swapEndianFormat(Ints.toByteArray(quad)));
    int totalBits = bytes.length * 8;
    for (int i = 0; i < dims; i++) {
      prefixBitSet.set(totalBits - leftOffset + i, quadBitSet.get(32 - dims + i));
    }
    byte[] start = ByteArrayUtils.swapEndianFormat(prefixBitSet.toByteArray());
    if (start.length < bytes.length) {
      start = Arrays.copyOf(start, bytes.length);
    }
    prefixBitSet.set(totalBits - leftOffset + dims, totalBits);
    byte[] end = ByteArrayUtils.swapEndianFormat(prefixBitSet.toByteArray());
    if (end.length < bytes.length) {
      end = Arrays.copyOf(end, bytes.length);
    }
    return new ByteArrayRange(start, end);
  }

  @Override
  public byte[] toBinary() {
    final List<byte[]> dimensionDefBinaries = new ArrayList<>(dimensionDefs.length);
    int bufferLength = VarintUtils.unsignedIntByteLength(dimensionDefs.length);
    for (final SFCDimensionDefinition sfcDimension : dimensionDefs) {
      final byte[] sfcDimensionBinary = PersistenceUtils.toBinary(sfcDimension);
      bufferLength +=
          (sfcDimensionBinary.length
              + VarintUtils.unsignedIntByteLength(sfcDimensionBinary.length));
      dimensionDefBinaries.add(sfcDimensionBinary);
    }
    final ByteBuffer buf = ByteBuffer.allocate(bufferLength);
    VarintUtils.writeUnsignedInt(dimensionDefs.length, buf);
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
    dimensionDefs = new SFCDimensionDefinition[numDimensions];
    for (int i = 0; i < numDimensions; i++) {
      final byte[] dim = ByteArrayUtils.safeRead(buf, VarintUtils.readUnsignedInt(buf));
      dimensionDefs[i] = (SFCDimensionDefinition) PersistenceUtils.fromBinary(dim);
    }
    init(dimensionDefs);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    final String className = getClass().getName();
    result = (prime * result) + ((className == null) ? 0 : className.hashCode());
    result = (prime * result) + Arrays.hashCode(dimensionDefs);
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
    final ZOrderSFC other = (ZOrderSFC) obj;

    if (!Arrays.equals(dimensionDefs, other.dimensionDefs)) {
      return false;
    }
    return true;
  }

  @Override
  public long[] normalizeRange(final double minValue, final double maxValue, final int d) {
    return new long[] {
        (long) (dimensionDefs[d].normalize(minValue) * binsPerDimension),
        (long) (dimensionDefs[d].normalize(maxValue) * binsPerDimension)};
  }
}