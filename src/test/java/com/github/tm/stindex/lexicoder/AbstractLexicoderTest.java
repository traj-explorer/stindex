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
package com.github.tm.stindex.lexicoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractLexicoderTest<T extends Number & Comparable<T>> {
  private final NumberLexicoder<T> lexicoder;
  private final T expectedMin;
  private final T expectedMax;
  private final T[] unsortedVals;
  private final Comparator<byte[]> comparator;

  public AbstractLexicoderTest(
      final NumberLexicoder<T> lexicoder,
      final T expectedMin,
      final T expectedMax,
      final T[] unsortedVals,
      final Comparator<byte[]> comparator) {
    super();
    this.lexicoder = lexicoder;
    this.expectedMin = expectedMin;
    this.expectedMax = expectedMax;
    this.unsortedVals = unsortedVals;
    this.comparator = comparator;
  }

  @Test
  public void testRanges() {
    Assert.assertTrue(lexicoder.getMinimumValue().equals(expectedMin));
    Assert.assertTrue(lexicoder.getMaximumValue().equals(expectedMax));
  }

  @Test
  public void testSortOrder() {
    final List<T> list = Arrays.asList(unsortedVals);
    final Map<byte[], T> sortedByteArrayToRawTypeMappings = new TreeMap<>(comparator);
    for (final T d : list) {
      sortedByteArrayToRawTypeMappings.put(lexicoder.toByteArray(d), d);
    }
    Collections.sort(list);
    int idx = 0;
    final Set<byte[]> sortedByteArrays = sortedByteArrayToRawTypeMappings.keySet();
    for (final byte[] byteArray : sortedByteArrays) {
      final T value = sortedByteArrayToRawTypeMappings.get(byteArray);
      Assert.assertTrue(value.equals(list.get(idx++)));
    }
  }
}
