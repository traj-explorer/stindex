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

/**
 * A lexicoder for signed values (in the range from Byte.MIN_VALUE to Byte.MAX_VALUE). Does an
 * exclusive or on the most significant bit to invert the sign, so that lexicographic ordering of
 * the byte arrays matches the natural order of the numbers.
 */
public class ByteLexicoder implements NumberLexicoder<Byte> {

  protected ByteLexicoder() {}

  @Override
  public byte[] toByteArray(final Byte value) {
    return new byte[] {((byte) (value ^ 0x80))};
  }

  @Override
  public Byte fromByteArray(final byte[] bytes) {
    return (byte) (bytes[0] ^ 0x80);
  }

  @Override
  public Byte getMinimumValue() {
    return Byte.MIN_VALUE;
  }

  @Override
  public Byte getMaximumValue() {
    return Byte.MAX_VALUE;
  }
}
