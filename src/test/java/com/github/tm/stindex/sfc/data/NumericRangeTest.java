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
package com.github.tm.stindex.sfc.data;

import com.github.tm.stindex.spatial.sfc.data.NumericRange;
import org.junit.Assert;
import org.junit.Test;

public class NumericRangeTest {

  private final double MINIMUM = 20;
  private final double MAXIMUM = 100;
  private final double CENTROID = 60;
  private final double DELTA = 1e-15;

  @Test
  public void testNumericRangeValues() {

    final NumericRange numericRange = new NumericRange(MINIMUM, MAXIMUM);

    Assert.assertEquals(MINIMUM, numericRange.getMin(), DELTA);
    Assert.assertEquals(MAXIMUM, numericRange.getMax(), DELTA);
    Assert.assertEquals(CENTROID, numericRange.getCentroid(), DELTA);
    Assert.assertTrue(numericRange.isRange());
  }
}
