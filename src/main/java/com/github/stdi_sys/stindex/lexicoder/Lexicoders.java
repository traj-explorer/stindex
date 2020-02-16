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

/** A class containing instances of lexicoders. */
public class Lexicoders {
  public static final ByteLexicoder BYTE = new ByteLexicoder();
  public static final ShortLexicoder SHORT = new ShortLexicoder();
  public static final IntegerLexicoder INT = new IntegerLexicoder();
  public static final LongLexicoder LONG = new LongLexicoder();
  public static final DoubleLexicoder DOUBLE = new DoubleLexicoder();
  public static final FloatLexicoder FLOAT = new FloatLexicoder();
}
