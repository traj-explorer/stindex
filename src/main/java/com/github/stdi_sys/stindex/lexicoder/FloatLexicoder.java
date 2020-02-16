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

/** A lexicoder for preserving the native Java sort order of Float values. */
public class FloatLexicoder implements NumberLexicoder<Float> {

  @Override
  public byte[] toByteArray(final Float value) {
    int i = Float.floatToRawIntBits(value);
    if (i < 0) {
      i = ~i;
    } else {
      i = i ^ 0x80000000;
    }

    return Ints.toByteArray(i);
  }

  @Override
  public Float fromByteArray(final byte[] bytes) {
    int i = Ints.fromByteArray(bytes);
    if (i < 0) {
      i = i ^ 0x80000000;
    } else {
      i = ~i;
    }

    return Float.intBitsToFloat(i);
  }

  @Override
  public Float getMinimumValue() {
    return -Float.MAX_VALUE;
  }

  @Override
  public Float getMaximumValue() {
    return Float.MAX_VALUE;
  }
}
