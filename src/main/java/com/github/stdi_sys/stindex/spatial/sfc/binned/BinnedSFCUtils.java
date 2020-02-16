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
package com.github.stdi_sys.stindex.spatial.sfc.binned;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.stdi_sys.stindex.dimension.NumericDimensionDefinition;
import com.github.stdi_sys.stindex.dimension.bin.BinRange;
import com.github.stdi_sys.stindex.spatial.GridIndex;
import com.github.stdi_sys.stindex.spatial.sfc.RangeDecomposition;
import com.github.stdi_sys.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.stdi_sys.stindex.spatial.sfc.data.BinnedNumericDataset;
import com.github.stdi_sys.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericData;
import com.github.stdi_sys.stindex.spatial.sfc.data.NumericRange;
import com.github.stdi_sys.stindex.*;

public class BinnedSFCUtils {

  public static List<SinglePartitionQueryRanges> getQueryRanges(
      final List<BinnedNumericDataset> binnedQueries,
      final GridIndex sfc,
      final int maxRanges,
      final Byte tier) {
    final List<SinglePartitionQueryRanges> queryRanges = new ArrayList<>();

    int maxRangeDecompositionPerBin = maxRanges;
    if ((maxRanges > 1) && (binnedQueries.size() > 1)) {
      maxRangeDecompositionPerBin =
          (int) Math.ceil((double) maxRanges / (double) binnedQueries.size());
    }
    for (final BinnedNumericDataset binnedQuery : binnedQueries) {
      final RangeDecomposition rangeDecomp =
          sfc.decomposeRange(binnedQuery, maxRangeDecompositionPerBin);
      final byte[] tierAndBinId = tier != null ? ByteArrayUtils.combineArrays(new byte[] {tier
          // we're assuming tiers only go to 127 (the max byte
          // value)
      }, binnedQuery.getBinId()) : binnedQuery.getBinId();

      queryRanges.add(
          new SinglePartitionQueryRanges(tierAndBinId, Arrays.asList(rangeDecomp.getRanges())));
    }
    return queryRanges;
  }

  public static MultiDimensionalCoordinateRanges getCoordinateRanges(
      final BinRange[][] binRangesPerDimension,
      final GridIndex sfc,
      final int numDimensions,
      final Byte tier) {
    final CoordinateRange[][] coordinateRangesPerDimension = new CoordinateRange[numDimensions][];
    for (int d = 0; d < coordinateRangesPerDimension.length; d++) {
      coordinateRangesPerDimension[d] = new CoordinateRange[binRangesPerDimension[d].length];
      for (int i = 0; i < binRangesPerDimension[d].length; i++) {
        final long[] range =
            sfc.normalizeRange(
                binRangesPerDimension[d][i].getNormalizedMin(),
                binRangesPerDimension[d][i].getNormalizedMax(),
                d);
        coordinateRangesPerDimension[d][i] =
            new CoordinateRange(range[0], range[1], binRangesPerDimension[d][i].getBinId());
      }
    }
    if (tier == null) {
      return new MultiDimensionalCoordinateRanges(new byte[0], coordinateRangesPerDimension);
    }
    return new MultiDimensionalCoordinateRanges(new byte[] {tier}, coordinateRangesPerDimension);
  }

  public static SinglePartitionInsertionIds getSingleBinnedInsertionId(
      final BigInteger rowCount,
      final Byte multiDimensionalId,
      final BinnedNumericDataset index,
      final GridIndex sfc) {
    if (rowCount.equals(BigInteger.ONE)) {
      final byte[] tierAndBinId =
          multiDimensionalId != null
              ? ByteArrayUtils.combineArrays(new byte[] {multiDimensionalId}, index.getBinId())
              : index.getBinId();
      final double[] minValues = index.getMinValuesPerDimension();
      final double[] maxValues = index.getMaxValuesPerDimension();
      byte[] singleId = null;
      if (Arrays.equals(maxValues, minValues)) {
        singleId = sfc.getIndex(minValues).getBytes();
      } else {
        final byte[] minId = sfc.getIndex(minValues).getBytes();
        final byte[] maxId = sfc.getIndex(maxValues).getBytes();

        if (Arrays.equals(minId, maxId)) {
          singleId = minId;
        }
      }
      if (singleId != null) {
        return new SinglePartitionInsertionIds(tierAndBinId, singleId);
      }
    }
    return null;
  }

  public static Coordinate[] getCoordinatesForId(
      final byte[] rowId,
      final NumericDimensionDefinition[] baseDefinitions,
      final GridIndex sfc) {
    final SFCIdAndBinInfo sfcIdAndBinInfo = getSFCIdAndBinInfo(rowId, baseDefinitions);
    final long[] coordinateValues = sfc.getCoordinates(new ByteArray(sfcIdAndBinInfo.sfcId));
    if (coordinateValues == null) {
      return null;
    }
    final Coordinate[] retVal = new Coordinate[coordinateValues.length];
    for (int i = 0; i < coordinateValues.length; i++) {
      final byte[] bin = sfcIdAndBinInfo.binIds.get(i);
      retVal[i] = new Coordinate(coordinateValues[i], bin);
    }
    return retVal;
  }

  public static MultiDimensionalNumericData getRangeForId(
      final byte[] rowId,
      final NumericDimensionDefinition[] baseDefinitions,
      final GridIndex sfc) {
    final SFCIdAndBinInfo sfcIdAndBinInfo = getSFCIdAndBinInfo(rowId, baseDefinitions);
    final MultiDimensionalNumericData numericData = sfc.getRanges(new ByteArray(sfcIdAndBinInfo.sfcId));
    // now we need to unapply the bins to the data, denormalizing the
    // ranges to the native bounds
    if (sfcIdAndBinInfo.rowIdOffset > 1) {
      final NumericData[] data = numericData.getDataPerDimension();
      for (final Entry<Integer, byte[]> entry : sfcIdAndBinInfo.binIds.entrySet()) {
        final int dimension = entry.getKey();
        final NumericRange range =
            baseDefinitions[dimension].getDenormalizedRange(
                new BinRange(
                    entry.getValue(),
                    data[dimension].getMin(),
                    data[dimension].getMax(),
                    false));
        data[dimension] = range;
      }
      return new BasicNumericDataset(data);
    }
    return numericData;
  }

  private static SFCIdAndBinInfo getSFCIdAndBinInfo(
      final byte[] rowId,
      final NumericDimensionDefinition[] baseDefinitions) {

    final Map<Integer, byte[]> binIds = new HashMap<>();
    // one for the tier
    int rowIdOffset = 1;
    for (int dimensionIdx = 0; dimensionIdx < baseDefinitions.length; dimensionIdx++) {
      final int binSize = baseDefinitions[dimensionIdx].getFixedBinIdSize();
      if (binSize > 0) {
        binIds.put(dimensionIdx, Arrays.copyOfRange(rowId, rowIdOffset, rowIdOffset + binSize));
        rowIdOffset += binSize;
      }
    }
    final byte[] sfcId = Arrays.copyOfRange(rowId, rowIdOffset, rowId.length);
    return new SFCIdAndBinInfo(sfcId, binIds, rowIdOffset);
  }

  private static class SFCIdAndBinInfo {
    private final byte[] sfcId;
    private final Map<Integer, byte[]> binIds;
    private final int rowIdOffset;

    public SFCIdAndBinInfo(
        final byte[] sfcId,
        final Map<Integer, byte[]> binIds,
        final int rowIdOffset) {
      super();
      this.sfcId = sfcId;
      this.binIds = binIds;
      this.rowIdOffset = rowIdOffset;
    }
  }
}
