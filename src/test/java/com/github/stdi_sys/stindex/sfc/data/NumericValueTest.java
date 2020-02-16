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
package com.github.stdi_sys.stindex.sfc.data;

import com.github.stdi_sys.stindex.spatial.sfc.data.NumericValue;
import org.junit.Assert;
import org.junit.Test;

public class NumericValueTest {

  private final double VALUE = 50;
  private final double DELTA = 1e-15;

  @Test
  public void testNumericValue() {

    final NumericValue numericValue = new NumericValue(VALUE);

    Assert.assertEquals(VALUE, numericValue.getMin(), DELTA);
    Assert.assertEquals(VALUE, numericValue.getMax(), DELTA);
    Assert.assertEquals(VALUE, numericValue.getCentroid(), DELTA);
    Assert.assertFalse(numericValue.isRange());
  }
}
