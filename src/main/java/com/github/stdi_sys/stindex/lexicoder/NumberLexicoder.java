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
 * A lexicoder for a number type. Converts back and forth between a number and a byte array. A
 * lexicographical sorting of the byte arrays will yield the natural order of the numbers that they
 * represent.
 *
 * @param <T> a number type
 */
public interface NumberLexicoder<T extends Number> {
  /**
   * Get a byte[] that represents the number value.
   *
   * @param value a number
   * @return the byte array representing the number
   */
  public byte[] toByteArray(T value);

  /**
   * Get the value of a byte array
   *
   * @param bytes a byte array representing a number
   * @return the number
   */
  public T fromByteArray(byte[] bytes);

  /**
   * Get the minimum value of the range of numbers that this lexicoder can encode and decode (i.e.
   * the number represented by all 0 bits).
   *
   * @return the minimum value in the lexicoder's range
   */
  public T getMinimumValue();

  /**
   * Get the maximum value of the range of numbers that this lexicoder can encode and decode (i.e.
   * the number represented by all 1 bits).
   *
   * @return the maximum value in the lexicoder's range
   */
  public T getMaximumValue();
}
