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

public class FloatLexicoderTest extends AbstractLexicoderTest<Float> {
  public FloatLexicoderTest() {
    super(
        Lexicoders.FLOAT,
        -Float.MAX_VALUE,
        Float.MAX_VALUE,
        new Float[] {
            -10f,
            -Float.MAX_VALUE,
            11f,
            -14.2f,
            14.2f,
            -100.002f,
            100.002f,
            -11f,
            Float.MAX_VALUE,
            0f},
        UnsignedBytes.lexicographicalComparator());
  }
}
