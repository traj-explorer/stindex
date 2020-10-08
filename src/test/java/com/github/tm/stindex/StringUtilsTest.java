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
package com.github.tm.stindex;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StringUtilsTest {
  @Test
  public void testFull() {
    final String[] result =
        StringUtils.stringsFromBinary(StringUtils.stringsToBinary(new String[] {"12", "34"}));
    assertEquals(2, result.length);
    assertEquals("12", result[0]);
    assertEquals("34", result[1]);
  }

  @Test
  public void testEmpty() {
    final String[] result =
        StringUtils.stringsFromBinary(StringUtils.stringsToBinary(new String[] {}));
    assertEquals(0, result.length);
  }
}
