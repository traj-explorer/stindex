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

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

public class ByteArrayUtilsTest {

  @Test
  public void testSplit() {
    final ByteArray first = new ByteArray("first");
    final ByteArray second = new ByteArray("second");
    final byte[] combined =
        ByteArrayUtils.combineVariableLengthArrays(first.getBytes(), second.getBytes());
    final Pair<byte[], byte[]> split = ByteArrayUtils.splitVariableLengthArrays(combined);
    Assert.assertArrayEquals(first.getBytes(), split.getLeft());
    Assert.assertArrayEquals(second.getBytes(), split.getRight());
  }

  @Test
  public void testVariableLengthEncodeDecode() {
    testVariableLengthValue(0);
    testVariableLengthValue(123456L);
    testVariableLengthValue(-42L);
    testVariableLengthValue(Byte.MAX_VALUE);
    testVariableLengthValue(Byte.MIN_VALUE);
    testVariableLengthValue(Integer.MIN_VALUE);
    testVariableLengthValue(Integer.MAX_VALUE);
    testVariableLengthValue(Long.MAX_VALUE);
    testVariableLengthValue(Long.MIN_VALUE);
  }

  @Test
  public void getNextPrefixTest() {
    byte[] bytes = new byte[] {1};
    byte[] res = ByteArrayUtils.getNextPrefix(bytes);

  }

  private void testVariableLengthValue(final long value) {
    final byte[] encoded = ByteArrayUtils.variableLengthEncode(value);
    final long result = ByteArrayUtils.variableLengthDecode(encoded);
    Assert.assertEquals(value, result);
  }
}
