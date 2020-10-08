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
package com.github.tm.stindex.dimension.bin;

import java.nio.ByteBuffer;
import org.junit.Assert;
import org.junit.Test;

public class BinRangeTest {

  private final double MINIMUM_RANGE = 20;
  private final double MAXIMUM_RANGE = 100;
  private final double DELTA = 1e-15;

  @Test
  public void testBinRangeValues() {

    final BinRange binRange = new BinRange(MINIMUM_RANGE, MAXIMUM_RANGE);

    Assert.assertEquals(MINIMUM_RANGE, binRange.getNormalizedMin(), DELTA);
    Assert.assertEquals(MAXIMUM_RANGE, binRange.getNormalizedMax(), DELTA);

    Assert.assertFalse(binRange.isFullExtent());
  }

  @Test
  public void testBinRangeFullExtent() {

    final int binIdValue = 120;
    final byte[] binID = ByteBuffer.allocate(4).putInt(binIdValue).array();
    final boolean fullExtent = true;

    final BinRange binRange = new BinRange(binID, MINIMUM_RANGE, MAXIMUM_RANGE, fullExtent);

    Assert.assertEquals(MINIMUM_RANGE, binRange.getNormalizedMin(), DELTA);
    Assert.assertEquals(MAXIMUM_RANGE, binRange.getNormalizedMax(), DELTA);

    Assert.assertTrue(binRange.isFullExtent());
  }
}
