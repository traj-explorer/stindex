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
package com.github.stdi_sys.stindex.lexicoder;

import com.google.common.primitives.Ints;

/**
 * A lexicoder for signed integers (in the range from Integer.MIN_VALUE to Integer.MAX_VALUE). Does
 * an exclusive or on the most significant bit to invert the sign, so that lexicographic ordering of
 * the byte arrays matches the natural order of the numbers.
 *
 * <p> See Apache Accumulo (org.apache.accumulo.core.client.lexicoder.IntegerLexicoder)
 */
public class IntegerLexicoder implements NumberLexicoder<Integer> {

  protected IntegerLexicoder() {}

  @Override
  public byte[] toByteArray(final Integer value) {
    return Ints.toByteArray(value ^ 0x80000000);
  }

  @Override
  public Integer fromByteArray(final byte[] bytes) {
    final int value = Ints.fromByteArray(bytes);
    return value ^ 0x80000000;
  }

  @Override
  public Integer getMinimumValue() {
    return Integer.MIN_VALUE;
  }

  @Override
  public Integer getMaximumValue() {
    return Integer.MAX_VALUE;
  }
}
