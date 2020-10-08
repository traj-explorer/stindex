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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.github.tm.stindex.persist.Persistable;
import com.github.tm.stindex.persist.PersistenceUtils;
import org.junit.Assert;
import org.junit.Test;

public class PersistenceUtilsTest {

  public static class APersistable implements Persistable {

    @Override
    public byte[] toBinary() {
      return new byte[] {1, 2, 3};
    }

    @Override
    public void fromBinary(final byte[] bytes) {
      Assert.assertTrue(Arrays.equals(bytes, new byte[] {1, 2, 3}));
    }
  }

  @Test
  public void test() {
    final APersistable persistable = new APersistable();
    Assert.assertTrue(
        PersistenceUtils.fromBinaryAsList(
            PersistenceUtils.toBinary(new ArrayList<Persistable>())).isEmpty());
    Assert.assertTrue(
        PersistenceUtils.fromBinaryAsList(
            PersistenceUtils.toBinary(
                Collections.<Persistable>singleton(persistable))).size() == 1);

    Assert.assertTrue(
        PersistenceUtils.fromBinaryAsList(
            PersistenceUtils.toBinary(
                Arrays.<Persistable>asList(
                    new Persistable[] {persistable, persistable}))).size() == 2);
  }
}
