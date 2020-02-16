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
package com.github.stdi_sys.stindex;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class ByteArrayTest {

  @Test
  public void fromLongTest() {
    ByteArray byteArray = new ByteArray(1234567890L);
    Assert.assertEquals(1234567890, byteArray.toLong());
  }

  @Test
  public void test() {
    LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 0, 0);
    System.out.println(dateTime);
  }

}