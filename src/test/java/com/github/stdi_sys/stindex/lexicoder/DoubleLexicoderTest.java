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

import com.google.common.primitives.UnsignedBytes;

public class DoubleLexicoderTest extends AbstractLexicoderTest<Double> {
  public DoubleLexicoderTest() {
    super(
        Lexicoders.DOUBLE,
        -Double.MAX_VALUE,
        Double.MAX_VALUE,
        new Double[] {
            -10d,
            -Double.MAX_VALUE,
            11d,
            -14.2,
            14.2,
            -100.002,
            100.002,
            -11d,
            Double.MAX_VALUE,
            0d},
        UnsignedBytes.lexicographicalComparator());
  }
}
