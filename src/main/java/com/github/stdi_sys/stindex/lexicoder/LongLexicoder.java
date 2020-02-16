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

/**
 * A lexicoder for signed integers (in the range from Long.MIN_VALUE to Long.MAX_VALUE). Does an
 * exclusive or on the most significant bit to invert the sign, so that lexicographic ordering of
 * the byte arrays matches the natural order of the numbers.
 *
 * <p> See Apache Accumulo (org.apache.accumulo.core.client.lexicoder.LongLexicoder)
 */
public class LongLexicoder implements NumberLexicoder<Long> {

  protected LongLexicoder() {}

  @Override
  public byte[] toByteArray(final Long value) {
    return Longs.toByteArray(value ^ 0x8000000000000000l);
  }

  @Override
  public Long fromByteArray(final byte[] bytes) {
    final long value = Longs.fromByteArray(bytes);
    return value ^ 0x8000000000000000l;
  }

  @Override
  public Long getMinimumValue() {
    return Long.MIN_VALUE;
  }

  @Override
  public Long getMaximumValue() {
    return Long.MAX_VALUE;
  }
}
