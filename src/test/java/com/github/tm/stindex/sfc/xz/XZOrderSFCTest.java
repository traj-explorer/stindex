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
package com.github.tm.stindex.sfc.xz;

import com.github.tm.stindex.dimension.BasicDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.SFCDimensionDefinition;
import com.github.tm.stindex.spatial.sfc.data.BasicNumericDataset;
import com.github.tm.stindex.spatial.sfc.data.MultiDimensionalNumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericData;
import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import com.github.tm.stindex.spatial.sfc.xz.XZOrderSFC;
import org.junit.Assert;
import org.junit.Test;

public class XZOrderSFCTest {

  @Test
  public void testIndex() {
    final double[] values = {42, 43, 57, 59};
    // TODO Meaningful examination of results?
    Assert.assertNotNull(createSFC().getIndex(values));
  }

  @Test
  public void testRangeDecomposition() {
    final NumericRange longBounds = new NumericRange(19.0, 21.0);
    final NumericRange latBounds = new NumericRange(33.0, 34.0);
    final NumericData[] dataPerDimension = {longBounds, latBounds};
    final MultiDimensionalNumericData query = new BasicNumericDataset(dataPerDimension);
    // TODO Meaningful examination of results?
    Assert.assertNotNull(createSFC().decomposeRangeFully(query));
  }

  private XZOrderSFC createSFC() {
    final SFCDimensionDefinition[] dimensions =
        {
            new SFCDimensionDefinition(new BasicDimensionDefinition(-180.0, 180.0), 32),
            new SFCDimensionDefinition(new BasicDimensionDefinition(-90.0, 90.0), 32)};
    return new XZOrderSFC(dimensions);
  }
}
