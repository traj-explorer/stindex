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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;

public class ByteArrayRangeTest {

  @Test
  public void testUnion() {
    final ByteArrayRange bar1 =
        new ByteArrayRange(new ByteArray("232").getBytes(), new ByteArray("332").getBytes());
    final ByteArrayRange bar2 =
        new ByteArrayRange(new ByteArray("282").getBytes(), new ByteArray("300").getBytes());
    final ByteArrayRange bar3 =
        new ByteArrayRange(new ByteArray("272").getBytes(), new ByteArray("340").getBytes());
    final ByteArrayRange bar4 =
        new ByteArrayRange(new ByteArray("392").getBytes(), new ByteArray("410").getBytes());

    Collection<ByteArrayRange> l1 = new ArrayList<>(Arrays.asList(bar4, bar3, bar1, bar2));
    l1 = ByteArrayRange.mergeIntersections(l1, ByteArrayRange.MergeOperation.UNION);

    Collection<ByteArrayRange> l2 = new ArrayList<>(Arrays.asList(bar1, bar4, bar2, bar3));
    l2 = ByteArrayRange.mergeIntersections(l2, ByteArrayRange.MergeOperation.UNION);

    assertEquals(2, l1.size());

    assertEquals(l1, l2);

    assertEquals(
        new ByteArrayRange(new ByteArray("232").getBytes(), new ByteArray("340").getBytes()),
        ((ArrayList<ByteArrayRange>) l1).get(0));
    assertEquals(
        new ByteArrayRange(new ByteArray("392").getBytes(), new ByteArray("410").getBytes()),
        ((ArrayList<ByteArrayRange>) l1).get(1));
  }

  @Test
  public void testIntersection() {
    final ByteArrayRange bar1 =
        new ByteArrayRange(new ByteArray("232").getBytes(), new ByteArray("332").getBytes());
    final ByteArrayRange bar2 =
        new ByteArrayRange(new ByteArray("282").getBytes(), new ByteArray("300").getBytes());
    final ByteArrayRange bar3 =
        new ByteArrayRange(new ByteArray("272").getBytes(), new ByteArray("340").getBytes());
    final ByteArrayRange bar4 =
        new ByteArrayRange(new ByteArray("392").getBytes(), new ByteArray("410").getBytes());

    Collection<ByteArrayRange> l1 = new ArrayList<>(Arrays.asList(bar4, bar3, bar1, bar2));
    l1 = ByteArrayRange.mergeIntersections(l1, ByteArrayRange.MergeOperation.INTERSECTION);

    Collection<ByteArrayRange> l2 = new ArrayList<>(Arrays.asList(bar1, bar4, bar2, bar3));
    l2 = ByteArrayRange.mergeIntersections(l2, ByteArrayRange.MergeOperation.INTERSECTION);

    assertEquals(2, l1.size());

    assertEquals(l1, l2);

    assertEquals(
        new ByteArrayRange(new ByteArray("282").getBytes(), new ByteArray("300").getBytes()),
        ((ArrayList<ByteArrayRange>) l1).get(0));
    assertEquals(
        new ByteArrayRange(new ByteArray("392").getBytes(), new ByteArray("410").getBytes()),
        ((ArrayList<ByteArrayRange>) l1).get(1));
  }

  final Random random = new Random();

  public String increment(final String id) {
    int v = (int) (Math.abs(random.nextDouble()) * 10000);
    final StringBuffer buf = new StringBuffer();
    int pos = id.length() - 1;
    int r = 0;
    while (v > 0) {
      final int m = (v - ((v >> 8) << 8));
      final int c = id.charAt(pos);
      final int n = c + m + r;
      buf.append((char) (n % 255));
      r = n / 255;
      v >>= 8;
      pos--;
    }
    while (pos >= 0) {
      buf.append(id.charAt(pos--));
    }
    return buf.reverse().toString();
  }

  @Test
  public void bigTest() {
    final List<ByteArrayRange> l2 = new ArrayList<>();
    for (int i = 0; i < 3000; i++) {
      String seed = UUID.randomUUID().toString();
      for (int j = 0; j < 500; j++) {
        l2.add(
            new ByteArrayRange(
                new ByteArray(seed).getBytes(),
                new ByteArray(increment(seed)).getBytes()));
        seed = increment(seed);
      }
    }

    ByteArrayRange.mergeIntersections(l2, ByteArrayRange.MergeOperation.INTERSECTION);
  }
}
