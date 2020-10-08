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

import com.google.common.primitives.Shorts;

/**
 * A lexicoder for signed integers (in the range from Short.MIN_VALUE to Short.MAX_VALUE). Does an
 * exclusive or on the most significant bit to invert the sign, so that lexicographic ordering of
 * the byte arrays matches the natural order of the numbers.
 */
public class ShortLexicoder implements NumberLexicoder<Short> {

  protected ShortLexicoder() {}

  @Override
  public byte[] toByteArray(final Short value) {
    return Shorts.toByteArray((short) (value ^ 0x8000));
  }

  @Override
  public Short fromByteArray(final byte[] bytes) {
    final short value = Shorts.fromByteArray(bytes);
    return (short) (value ^ 0x8000);
  }

  @Override
  public Short getMinimumValue() {
    return Short.MIN_VALUE;
  }

  @Override
  public Short getMaximumValue() {
    return Short.MAX_VALUE;
  }
}
