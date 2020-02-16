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

import com.google.common.primitives.Longs;

/** A lexicoder for preserving the native Java sort order of Double values. */
public class DoubleLexicoder implements NumberLexicoder<Double> {

  @Override
  public byte[] toByteArray(final Double value) {
    long l = Double.doubleToRawLongBits(value);
    if (l < 0) {
      l = ~l;
    } else {
      l = l ^ 0x8000000000000000l;
    }
    return Longs.toByteArray(l);
  }

  @Override
  public Double fromByteArray(final byte[] bytes) {
    long l = Longs.fromByteArray(bytes);
    if (l < 0) {
      l = l ^ 0x8000000000000000l;
    } else {
      l = ~l;
    }
    return Double.longBitsToDouble(l);
  }

  @Override
  public Double getMinimumValue() {
    return -Double.MAX_VALUE;
  }

  @Override
  public Double getMaximumValue() {
    return Double.MAX_VALUE;
  }
}
