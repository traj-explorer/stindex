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

import com.github.tm.stindex.dimension.BasicDimensionDefinition;
import com.github.tm.stindex.dimension.UnboundedDimensionDefinition;
import com.github.tm.stindex.persist.PersistableRegistrySpi;
import com.github.tm.stindex.spatial.sfc.BasicSFCIndexStrategy;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.BinnedNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import com.github.tm.stindex.spatial.sfc.data.NumericValue;
import com.github.tm.stindex.spatial.sfc.hilbert.HilbertSFC;
import com.github.tm.stindex.spatial.sfc.tiered.SingleTierSubStrategy;
import com.github.tm.stindex.spatial.sfc.tiered.TieredSFCIndexStrategy;
import com.github.tm.stindex.spatial.sfc.xz.XZHierarchicalIndexStrategy;
import com.github.tm.stindex.spatial.sfc.xz.XZOrderSFC;
import com.github.tm.stindex.spatial.sfc.zorder.ZOrderSFC;
import com.github.tm.stindex.simple.HashKeyIndexStrategy;
import com.github.tm.stindex.simple.RoundRobinKeyIndexStrategy;
import com.github.tm.stindex.simple.SimpleByteIndexStrategy;
import com.github.tm.stindex.simple.SimpleDoubleIndexStrategy;
import com.github.tm.stindex.simple.SimpleFloatIndexStrategy;
import com.github.tm.stindex.simple.SimpleIntegerIndexStrategy;
import com.github.tm.stindex.simple.SimpleLongIndexStrategy;
import com.github.tm.stindex.simple.SimpleShortIndexStrategy;

public class IndexPersistableRegistry implements PersistableRegistrySpi {

  @Override
  public PersistableIdAndConstructor[] getSupportedPersistables() {
    return new PersistableIdAndConstructor[] {
        new PersistableIdAndConstructor((short) 100, CompoundIndexStrategy.CompoundIndexMetaDataWrapper::new),
        new PersistableIdAndConstructor((short) 101, TieredSFCIndexStrategy.TierIndexMetaData::new),
        new PersistableIdAndConstructor((short) 102, CompoundIndexStrategy::new),
        new PersistableIdAndConstructor((short) 103, CoordinateRange::new),
        new PersistableIdAndConstructor((short) 104, MultiDimensionalCoordinateRanges::new),
        new PersistableIdAndConstructor((short) 105, MultiDimensionalCoordinateRangesArray.ArrayOfArrays::new),
        new PersistableIdAndConstructor((short) 106, MultiDimensionalCoordinateRangesArray::new),
        new PersistableIdAndConstructor((short) 107, NullNumericIndexStrategy::new),
        new PersistableIdAndConstructor((short) 108, NumericIndexStrategyWrapper::new),
        new PersistableIdAndConstructor((short) 109, BasicDimensionDefinition::new),
        new PersistableIdAndConstructor((short) 110, UnboundedDimensionDefinition::new),
        new PersistableIdAndConstructor((short) 111, SFCDimensionDefinition::new),
        new PersistableIdAndConstructor((short) 112, BasicNumericDataset::new),
        new PersistableIdAndConstructor((short) 113, BinnedNumericDataset::new),
        new PersistableIdAndConstructor((short) 114, NumericRange::new),
        new PersistableIdAndConstructor((short) 115, NumericValue::new),
        new PersistableIdAndConstructor((short) 116, HilbertSFC::new),
        new PersistableIdAndConstructor((short) 117, SingleTierSubStrategy::new),
        new PersistableIdAndConstructor((short) 118, TieredSFCIndexStrategy::new),
        new PersistableIdAndConstructor((short) 119, XZHierarchicalIndexStrategy::new),
        new PersistableIdAndConstructor((short) 120, XZOrderSFC::new),
        new PersistableIdAndConstructor((short) 121, ZOrderSFC::new),
        new PersistableIdAndConstructor((short) 122, HashKeyIndexStrategy::new),
        new PersistableIdAndConstructor((short) 123, RoundRobinKeyIndexStrategy::new),
        new PersistableIdAndConstructor((short) 124, SimpleIntegerIndexStrategy::new),
        new PersistableIdAndConstructor((short) 125, SimpleLongIndexStrategy::new),
        new PersistableIdAndConstructor((short) 126, SimpleShortIndexStrategy::new),
        new PersistableIdAndConstructor((short) 127, XZHierarchicalIndexStrategy.XZHierarchicalIndexMetaData::new),
        new PersistableIdAndConstructor((short) 128, InsertionIds::new),
        new PersistableIdAndConstructor((short) 129, PartitionIndexStrategyWrapper::new),
        new PersistableIdAndConstructor((short) 130, SinglePartitionInsertionIds::new),
        new PersistableIdAndConstructor((short) 131, SimpleFloatIndexStrategy::new),
        new PersistableIdAndConstructor((short) 132, SimpleDoubleIndexStrategy::new),
        new PersistableIdAndConstructor((short) 133, SimpleByteIndexStrategy::new),
        new PersistableIdAndConstructor((short) 134, BasicSFCIndexStrategy::new),};
  }
}
